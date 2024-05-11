package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.AuthenticationException;
import roomescape.domain.role.MemberRole;
import roomescape.domain.role.Role;

public class PermissionCheckInterceptor implements HandlerInterceptor {
    private final TokenManager tokenManager;

    public PermissionCheckInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        PermissionRequired permissionRequired = method.getAnnotation(PermissionRequired.class);
        if (permissionRequired == null) {
            return true;
        }
        Role requiredRole = permissionRequired.value();
        String requestToken = AuthInformationExtractor.extractToken(request);
        MemberRole memberRole = tokenManager.extract(requestToken);
        if (!memberRole.hasRoleOf(requiredRole)) {
            throw new AuthenticationException();
        }
        return true;
    }
}
