package roomescape.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class CookieExtractor {

    public static Cookie extract(
            final HttpServletRequest request,
            final Predicate<Cookie> condition
    ) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new NoSuchElementException();
        }

        return Arrays.stream(cookies)
                .filter(condition)
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    public static String extractToken(final HttpServletRequest request) {
        return extract(request, cookie -> cookie.getName().equals("token"))
                .getValue();
    }
}
