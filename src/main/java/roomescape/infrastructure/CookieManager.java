package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {
    private static final String TOKEN_NAME = "token";

    public Cookie createCookie(final String token) {
        final Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie expireCookie() {
        final Cookie cookie = new Cookie(TOKEN_NAME, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String extractToken(final Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
