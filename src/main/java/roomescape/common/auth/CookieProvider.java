package roomescape.common.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    public Cookie createCookie(final String name, final String accessToken) {
        final Cookie cookie = new Cookie(name, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie createExpireCookie(final String name) {
        final Cookie expireCookie = new Cookie(name, null);
        expireCookie.setMaxAge(0);
        expireCookie.setPath("/");
        return expireCookie;
    }

}
