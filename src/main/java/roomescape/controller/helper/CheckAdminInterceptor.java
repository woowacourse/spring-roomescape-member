package roomescape.controller.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.MemberRole;
import roomescape.exception.auth.AccessDeniedException;
import roomescape.service.AuthService;

public class CheckAdminInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public CheckAdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MemberRole role = authService.findMemberRoleByCookie(request.getCookies());
        if (role != MemberRole.ADMIN) {
            throw new AccessDeniedException();
        }
        return true;
    }
}
