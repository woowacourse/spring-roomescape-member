package roomescape.global.security;

import jakarta.servlet.http.Cookie;
import java.time.Duration;
import java.util.Arrays;
import org.springframework.http.ResponseCookie;
import roomescape.auth.exception.CookieNullException;
import roomescape.auth.exception.TokenIsEmptyException;

public class CookieUtil {
    private static final String ACCESS_TOKEN = "token";
    private static final Duration LOGIN_DURATION = Duration.ofMinutes(50);
    private static final Duration LOGOUT_DURATION = Duration.ZERO;

    private CookieUtil() {
    }

    public static ResponseCookie createCookieForLogin(String token) {
        return createCookie(token, LOGIN_DURATION);
    }

    public static ResponseCookie createCookieForLogout() {
        return createCookie("", LOGOUT_DURATION);
    }

    private static ResponseCookie createCookie(String token, Duration maxAge) {
        return ResponseCookie.from(ACCESS_TOKEN, token)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();
    }

    public static String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new CookieNullException();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> ACCESS_TOKEN.equals(cookie.getName()))
                .findAny()
                .map(Cookie::getValue)
                .orElseThrow(TokenIsEmptyException::new);
    }
}
