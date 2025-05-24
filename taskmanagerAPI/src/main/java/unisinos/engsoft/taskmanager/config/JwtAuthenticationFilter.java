package unisinos.engsoft.taskmanager.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String username = jwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(token, username)) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        sendErrorResponse(response, request, HttpServletResponse.SC_FORBIDDEN, "Invalid token");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            sendErrorResponse(response, request, HttpServletResponse.SC_FORBIDDEN, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   HttpServletRequest request,
                                   int status,
                                   String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        String json = String.format(
                "{" +
                        "\"timestamp\": \"%s\"," +
                        "\"status\": %d," +
                        "\"error\": \"%s\"," +
                        "\"message\": \"%s\"," +
                        "\"path\": \"%s\"" +
                        "}",
                java.time.OffsetDateTime.now(),
                status,
                (status == 403) ? "FORBIDDEN" : "UNAUTHORIZED",
                message,
                request.getRequestURI()
        );
        response.getWriter().write(json);
    }
}
