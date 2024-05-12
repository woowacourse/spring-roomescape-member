package roomescape.controller.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.MemberRole;
import roomescape.exception.auth.AccessDeniedException;
import roomescape.service.AuthService;

public class RoleAllowedInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public RoleAllowedInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(RoleAllowed.class)) {
            checkRoleAccess(method, request);
        }
        return true;
    }

    private void checkRoleAccess(Method method, HttpServletRequest request) {
        RoleAllowed annotation = method.getAnnotation(RoleAllowed.class);
        MemberRole roleAllowed = annotation.value();
        MemberRole currentRole = authService.findMemberRoleByCookie(request.getCookies());
        if (currentRole.isLowerThan(roleAllowed)) {
            throw new AccessDeniedException();
        }
    }
}
