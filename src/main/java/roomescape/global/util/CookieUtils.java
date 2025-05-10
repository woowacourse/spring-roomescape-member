package roomescape.global.util;

import jakarta.servlet.http.Cookie;
import java.util.Objects;
import java.util.Optional;

public class CookieUtils {

    public static Cookie createBasic(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public static Optional<Cookie> findFromCookiesByName(Cookie[] cookies, String name) {
        if (cookies == null) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), name)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

    public static Cookie toExpiredCookie(Cookie cookie) {
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
