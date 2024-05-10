package roomescape.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    private static final String SESSION_KEY = "token";

    private CookieUtil() {
    }

    public static void setCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(SESSION_KEY, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_KEY)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public static void expireToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(SESSION_KEY, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
