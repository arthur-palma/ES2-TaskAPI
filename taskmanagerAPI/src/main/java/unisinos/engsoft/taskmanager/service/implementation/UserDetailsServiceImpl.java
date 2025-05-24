package unisinos.engsoft.taskmanager.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import unisinos.engsoft.taskmanager.model.Users;
import unisinos.engsoft.taskmanager.repository.UserRepository;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED,"Token Invalido"));

        if (!user.isActive()) {
            throw new ResponseStatusException(UNAUTHORIZED,"Token Invalido");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                true,
                true,
                true,
                user.getAuthorities()
        );
    }
}