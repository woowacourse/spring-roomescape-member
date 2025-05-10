package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.custom.reason.auth.AuthNotExistsCookieException;

public class AuthorizationAdminInterceptor implements HandlerInterceptor {

    private static final String TOKEN_NAME = "token";

    private final AuthService authService;

    public AuthorizationAdminInterceptor(
            final AuthService authService
    ) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String token = extractToken(request);

        final LoginMember loginMember = authService.findLoginMemberByToken(token);
        if (loginMember == null || !loginMember.role().equals("ADMIN")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }

    private String extractToken(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        validateExistsCookies(cookies);

        return Arrays.stream(cookies)
                .filter(cookie -> Objects.equals(cookie.getName(), TOKEN_NAME))
                .map(Cookie::getValue)
                .findAny()
                .orElseThrow(() -> new AuthNotExistsCookieException());
    }

    private static void validateExistsCookies(final Cookie[] cookies) {
        if(cookies == null){
            throw new AuthNotExistsCookieException();
        }
    }
}
