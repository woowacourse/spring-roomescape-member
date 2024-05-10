package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class CookieExtractor {
    private CookieExtractor() {
    }

    public static Optional<Cookie> getCookie(HttpServletRequest webRequest, String name) {
        Cookie[] cookies = webRequest.getCookies();
        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst();
    }

}
