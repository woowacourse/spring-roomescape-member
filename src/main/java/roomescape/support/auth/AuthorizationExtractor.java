package roomescape.support.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.exception.InvalidAuthException;

@Component
public class AuthorizationExtractor {

    private static final String TOKEN_COOKIE_NAME = "token";

    public String extract(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new InvalidAuthException("쿠키가 존재하지 않습니다.");
        }

        for (final Cookie cookie : cookies) {
            if (TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new InvalidAuthException("쿠키에 토큰이 존재하지 않습니다.");
    }
}
