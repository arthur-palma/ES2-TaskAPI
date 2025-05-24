package unisinos.engsoft.taskmanager.service.implementation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import unisinos.engsoft.taskmanager.DTO.LoginRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.config.JwtUtil;
import unisinos.engsoft.taskmanager.model.Users;
import unisinos.engsoft.taskmanager.repository.UserRepository;
import unisinos.engsoft.taskmanager.service.interfaces.IAuthService;
import unisinos.engsoft.taskmanager.service.interfaces.IPasswordEncryptionService;
import unisinos.engsoft.taskmanager.service.interfaces.IUserValidation;


import static unisinos.engsoft.taskmanager.mapper.UserMapper.toDTO;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final IPasswordEncryptionService passwordEncryptionService;
    private final JwtUtil jwtUtil;
    private final IUserValidation userValidation;

    @Override
    public ResponseEntity<UserDTO> login(LoginRequest loginRequest, HttpServletResponse response) {
        Users user = userValidation.validateUserByEmail(loginRequest.getEmail());

        if (!passwordEncryptionService.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(24 * 60 * 60);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(toDTO(user));
    }

    public String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    public Users getAuthenticatedUser() {
        String username = getAuthenticatedUsername();
        if (username == null) {
            throw new RuntimeException("User not authenticated");
        }

        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
