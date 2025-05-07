package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    public Cookie create(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");

        return cookie;
    }

    public Cookie invalidate(Cookie[] cookies) {
        Cookie invalidatedCookie = new Cookie("token", "");
        invalidatedCookie.setMaxAge(0);
        invalidatedCookie.setPath("/");
        invalidatedCookie.setSecure(true);

        return invalidatedCookie;
    }
}
