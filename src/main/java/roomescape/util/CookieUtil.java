package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.context.request.NativeWebRequest;

public class CookieUtil {

    private static final String TOKEN = "token";
    private static final int TOKEN_MAX_AGE = 60 * 60; // 1 hour

    private CookieUtil() {
    }

    public static void setTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(TOKEN, token);
        cookie.setMaxAge(TOKEN_MAX_AGE);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    public static void clearTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    public static String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public static String extractTokenFromCookie(NativeWebRequest webRequest) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        return extractTokenFromCookie(request);
    }
}
