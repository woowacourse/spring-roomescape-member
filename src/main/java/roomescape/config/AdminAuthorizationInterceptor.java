package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.infra.JwtTokenProvider;
import roomescape.model.Role;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AdminAuthorizationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = jwtTokenProvider.extractTokenFromCookies(request.getCookies());
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Role role = jwtTokenProvider.getRole(token);
        if (role == null || !role.equals(Role.ADMIN)) {
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           return false;
        }
        return true;
    }
}
