package roomescape.util;

import jakarta.servlet.http.Cookie;

public class CookieManager {

    public static String extractTokenFromCookies(Cookie[] cookies) {

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
