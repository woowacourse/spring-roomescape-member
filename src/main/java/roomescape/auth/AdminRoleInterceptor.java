package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.role.Role;

public class AdminRoleInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public AdminRoleInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestToken = AuthInformationExtractor.extractToken(request);
        authService.validatePermission(requestToken, Role.ADMIN);
        return true;
    }
}
