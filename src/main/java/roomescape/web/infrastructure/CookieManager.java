package roomescape.web.infrastructure;

import jakarta.servlet.http.Cookie;
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
}
