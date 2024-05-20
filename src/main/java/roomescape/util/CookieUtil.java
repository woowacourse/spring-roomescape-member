package roomescape.util;

import java.util.Optional;
import java.util.function.Supplier;

import jakarta.servlet.http.Cookie;

public interface CookieUtil {

    Cookie create(String value);

    Cookie expired();

    Optional<String> extractValue(Cookie[] cookies);

    static <T extends Throwable> Cookie[] requireNonnull(Cookie[] cookies, Supplier<T> throwable) {
        if (cookies == null) {
            throwable.get();
        }
        return cookies;
    }
}
