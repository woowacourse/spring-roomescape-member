package roomescape.util;

import jakarta.servlet.http.Cookie;

public class CookieManager {

    private static final String TOKEN_COOKIE_NAME = "token";

    public static String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
