package roomescape.controller.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.CustomException;
import roomescape.exception.CustomForbidden;
import roomescape.infrastructure.CookieProvider;

public class CheckAdminMemberInterceptor implements HandlerInterceptor {

    private final CookieProvider cookieProvider;

    public CheckAdminMemberInterceptor(final CookieProvider cookieProvider) {
        this.cookieProvider = cookieProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        try {
            final var authMember = cookieProvider.extractToken(request);
            if (authMember == null || !authMember.role().equals("ADMIN")) {
                throw new CustomForbidden("관리자가 아닙니다.");
            }
            return true;
        } catch (final Exception e) {
            response.setStatus(403);
            return false;
        }
    }
}
