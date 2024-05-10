package roomescape.config.util;

import jakarta.servlet.http.Cookie;

public class CookieExtractor {

    public CookieExtractor() {
    }

    public static String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
