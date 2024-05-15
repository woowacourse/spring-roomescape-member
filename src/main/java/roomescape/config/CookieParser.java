package roomescape.config;

import jakarta.servlet.http.Cookie;

import java.util.Objects;
import java.util.Optional;

public class CookieParser {

    private static final String TOKEN_NAME = "token";

    public static Optional<String> extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (Objects.equals(TOKEN_NAME, cookie.getName())) {
                return Optional.ofNullable(cookie.getValue());
            }
        }

        return Optional.empty();
    }
}
