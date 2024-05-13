package roomescape.utils;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import roomescape.exception.ResourceNotFoundException;

public class CookieParser {
    private CookieParser() {
    }

    public static Optional<String> searchValueFromKey(Cookie[] cookies, String key) throws ResourceNotFoundException {
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(key))
                .findFirst()
                .map(Cookie::getValue);
    }
}
