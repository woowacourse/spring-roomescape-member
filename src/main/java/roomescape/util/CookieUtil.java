package roomescape.util;

import java.util.Optional;
import java.util.function.Supplier;

import jakarta.servlet.http.Cookie;

public interface CookieUtil {

    Cookie create(String value);

    Cookie expired();

    Optional<String> extractValue(Cookie[] cookies);

    static <X extends RuntimeException> Cookie[] requireNonnull(
            Cookie[] cookies,
            Supplier<? extends X> exceptionSupplier
    ) throws X {
        if (cookies == null) {
            throw exceptionSupplier.get();
        }
        return cookies;
    }
}
