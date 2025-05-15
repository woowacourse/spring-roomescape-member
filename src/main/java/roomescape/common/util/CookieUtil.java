package roomescape.common.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public static Cookie addTokenCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie deleteTokenCookie() {
        Cookie cookie = new Cookie("token", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
