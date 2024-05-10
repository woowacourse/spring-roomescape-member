package roomescape.config;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Role;
import roomescape.service.JwtService;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public JwtAuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            response.sendRedirect("/login");
            return false;
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                Claims claims = jwtService.verifyToken(cookie.getValue());
                String role = claims.get("role", String.class);
                if (Role.getRole(role) == Role.ADMIN) {
                    return true;
                }
            }
        }
        response.sendRedirect("/");
        return false;
    }
}
