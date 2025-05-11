package roomescape.global.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.entity.LoginMember;
import roomescape.auth.entity.Role;
import roomescape.auth.service.MemberAuthService;
import roomescape.global.exception.forbidden.ForbiddenException;
import roomescape.global.infrastructure.TokenCookieProvider;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private final MemberAuthService authService;
    private final TokenCookieProvider tokenCookieProvider;

    public CheckAdminInterceptor(MemberAuthService authService, TokenCookieProvider tokenCookieProvider) {
        this.authService = authService;
        this.tokenCookieProvider = tokenCookieProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = tokenCookieProvider.extractToken(request);
        LoginMember loginMember = authService.getLoginMemberByToken(token);
        if (loginMember.hasRole(Role.ADMIN)) {
            return true;
        }
        throw new ForbiddenException("접근 권한이 없습니다.");
    }
}
