package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieExtractor {

    public String extract(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) return "";

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }

        return "";
    }
}
