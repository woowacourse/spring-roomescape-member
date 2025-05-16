package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class JwtExtractor {

    public static String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return extractTokenFromCookie(cookies);
    }

    private static String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }
}
