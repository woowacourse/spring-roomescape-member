package roomescape.common.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.auth.JwtExtractor;
import roomescape.common.exception.auth.InvalidAuthorizationException;
import roomescape.common.exception.auth.InvalidTokenException;
import roomescape.domain.member.LoginMember;
import roomescape.service.auth.AuthService;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final JwtExtractor jwtExtractor;

    public CheckLoginInterceptor(final AuthService authService, final JwtExtractor jwtExtractor) {
        this.authService = authService;
        this.jwtExtractor = jwtExtractor;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        final Cookie[] cookies = request.getCookies();
        validateNull(cookies);

        final String token = jwtExtractor.extractTokenFromCookie("token", cookies);
        final LoginMember loginMember = authService.findLoginMemberByToken(token);

        validateAuthorization(loginMember);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void validateNull(final Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            throw new InvalidTokenException("토큰을 찾을 수 없습니다.");
        }
    }

    private void validateAuthorization(final LoginMember loginMember) {
        if (isNotAdmin(loginMember)) {
            throw new InvalidAuthorizationException("접근 권한이 없습니다.");
        }
    }

    private boolean isNotAdmin(final LoginMember loginMember) {
        return loginMember == null || !loginMember.isAdmin();
    }
}
