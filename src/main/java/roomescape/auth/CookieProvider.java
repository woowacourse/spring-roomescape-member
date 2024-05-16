package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

public class CookieProvider {
    private static final String TOKEN_NAME = "token";

    public static String extractTokenFrom(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return extractTokenBy(cookies);
    }

    private static String extractTokenBy(final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public static void addTokenCookie(final HttpServletResponse response, final String accessToken) {
        final Cookie cookie = new Cookie(TOKEN_NAME, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        addCookie(response, cookie);
    }

    public static void expireTokenCookie(final HttpServletResponse response) {
        final Cookie cookie = new Cookie(TOKEN_NAME, null);
        cookie.setMaxAge(0);

        addCookie(response, cookie);
    }

    private static void addCookie(final HttpServletResponse response, final Cookie cookie) {
        response.addCookie(cookie);
    }
}
