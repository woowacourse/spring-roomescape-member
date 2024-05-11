package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.AuthService;
import roomescape.utils.CookieUtils;

public class CheckAdminInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public CheckAdminInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String token = CookieUtils.extractTokenFrom(request);
        final Member member = authService.findMemberByToken(token);

        if (member == null || isUser(member.getRole())) {
            response.setStatus(401);
            return false;
        }
        return true;
    }

    private boolean isUser(final Role role) {
        return role.isUser();
    }
}
