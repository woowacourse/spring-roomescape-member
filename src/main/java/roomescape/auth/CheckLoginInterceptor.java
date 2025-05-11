package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.LoginMember;
import roomescape.exception.InvalidAuthException;
import roomescape.service.AuthService;

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
        final String token = jwtExtractor.extractTokenFromCookie("token", cookies);
        final LoginMember loginMember = authService.findLoginMemberByToken(token);

        validateAuthorization(loginMember);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void validateAuthorization(final LoginMember loginMember) {
        if (isNotAdmin(loginMember)) {
            throw new InvalidAuthException("접근 권한이 없습니다.");
        }
    }

    private boolean isNotAdmin(final LoginMember loginMember) {
        return loginMember == null || !loginMember.isAdmin();
    }
}
