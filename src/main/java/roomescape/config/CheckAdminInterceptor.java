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
            throw new IllegalArgumentException("관리자 페이지는 관리자만 들어올 수 있습니다.");
        }
        return true;
    }

    private boolean isUser(final Role role) {
        return role.isUser();
    }
}
