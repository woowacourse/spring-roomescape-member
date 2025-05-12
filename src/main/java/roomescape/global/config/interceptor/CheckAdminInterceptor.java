package roomescape.global.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.service.dto.response.LoginMember;
import roomescape.auth.entity.Role;
import roomescape.auth.service.MemberAuthService;
import roomescape.global.exception.forbidden.ForbiddenException;
import roomescape.global.infrastructure.AuthTokenCookieProvider;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private final MemberAuthService authService;
    private final AuthTokenCookieProvider authTokenCookieProvider;

    public CheckAdminInterceptor(MemberAuthService authService, AuthTokenCookieProvider authTokenCookieProvider) {
        this.authService = authService;
        this.authTokenCookieProvider = authTokenCookieProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = authTokenCookieProvider.extractToken(request);
        LoginMember loginMember = authService.getLoginMemberByToken(token);
        if (loginMember.hasRole(Role.ADMIN)) {
            return true;
        }
        throw new ForbiddenException("접근 권한이 없습니다.");
    }
}
