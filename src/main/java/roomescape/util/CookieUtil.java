package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public class CookieUtil {

    public static Optional<Cookie> findCookie(final HttpServletRequest request, final String name) {
        final Cookie[] cookies = request.getCookies();
        final Cookie cookieByName = findCookieByName(name, cookies);

        if (cookieByName == null) {
            return Optional.empty();
        }

        return Optional.of(cookieByName);
    }

    private static Cookie findCookieByName(final String name, final Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (final Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }

    public static void addCookie(
            final HttpServletResponse response,
            final String name,
            final String value,
            final int maxAge
    ) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final String name
    ) {
        final Cookie[] cookies = request.getCookies();
        final Cookie cookieByName = findCookieByName(name, cookies);

        if (cookieByName != null) {
            cookieByName.setValue("");
            cookieByName.setPath("/");
            cookieByName.setMaxAge(0);
            response.addCookie(cookieByName);
        }
    }
}
