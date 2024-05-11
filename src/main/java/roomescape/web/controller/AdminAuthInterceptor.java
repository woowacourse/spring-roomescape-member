package roomescape.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.core.domain.Member;
import roomescape.core.service.AuthService;
import roomescape.web.exception.AuthorizationException;
import roomescape.web.infrastructure.CookieManager;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final CookieManager cookieManager;

    public AdminAuthInterceptor(final AuthService authService, final CookieManager cookieManager) {
        this.authService = authService;
        this.cookieManager = cookieManager;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String token = cookieManager.extractToken(request.getCookies());
        final Member member = authService.findMemberByToken(token);
        if (member == null || member.isNotAdmin()) {
            throw new AuthorizationException("접근 권한이 없습니다.");
        }
        return true;
    }
}
