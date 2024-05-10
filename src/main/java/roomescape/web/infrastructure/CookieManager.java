package roomescape.web.infrastructure;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {
    private static final String TOKEN_NAME = "token";

    public Cookie createCookie(final String token) {
        final Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String extractToken(final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
    }
}
