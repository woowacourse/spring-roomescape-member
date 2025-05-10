package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import roomescape.exception.AuthorizationException;

@Component
public class CookieExtractor {

    private static final String TOKEN_COOKIE_NAME = "token";

    public Cookie createCookie(final String token, final int age) {
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(age);
        return cookie;
    }

    public String extractToken(final Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            throw new AuthorizationException("쿠키가 존재하지 않습니다.");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthorizationException("쿠키 토큰을 추출할 수 없습니다."));
    }
}
