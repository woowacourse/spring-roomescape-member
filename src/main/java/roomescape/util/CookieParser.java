package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieParser {

    public static String getTokenCookie(final HttpServletRequest request, String target) {
        if (request == null || target == null) {
            throw new IllegalArgumentException("유효하지 않은 입력입니다");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (target.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
