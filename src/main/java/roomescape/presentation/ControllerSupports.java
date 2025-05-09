package roomescape.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class ControllerSupports {

    public static Optional<String> findCookieValueByKey(final HttpServletRequest request, final String key) {
        var cookies = request.getCookies();
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(key))
            .map(Cookie::getValue)
            .findAny();
    }
}
