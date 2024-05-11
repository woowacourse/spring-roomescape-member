package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.role.Role;

public class PermissionCheckInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public PermissionCheckInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(PermissionRequired.class)) {
            return true;
        }
        PermissionRequired permissionRequired = method.getAnnotation(PermissionRequired.class);
        Role requiredRole = permissionRequired.value();
        String token = AuthInformationExtractor.extractToken(request);
        authService.validatePermission(token, requiredRole);
        return true;
    }
}
