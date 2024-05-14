package roomescape.controller.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.exception.ForbiddenException;
import roomescape.infrastructure.TokenExtractor;
import roomescape.service.security.AuthService;

public class CheckPermissionInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckPermissionInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        Role resourceRole = extractResourceRole((HandlerMethod) handler);
        if (resourceRole.equals(Role.GUEST)) {
            return true;
        }

        String token = TokenExtractor.extract(request);
        Member member = authService.findMemberByToken(token);

        if (resourceRole.isHigherAuthority(member.getRole())) {
            throw new ForbiddenException("권한이 부족합니다.");
        }

        return true;
    }

    private Role extractResourceRole(HandlerMethod handler) {
        Permission classAnnotation = handler.getBean().getClass().getAnnotation(Permission.class);
        Permission methodAnnotation = handler.getMethod().getAnnotation(Permission.class);
        if (classAnnotation == null && methodAnnotation == null) {
            return Role.GUEST;
        }

        if (methodAnnotation != null) {
            return methodAnnotation.role();
        }

        return classAnnotation.role();
    }
}
