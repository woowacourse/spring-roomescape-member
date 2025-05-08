package roomescape.global.security;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.http.ResponseCookie;
import roomescape.auth.exception.CookieNotFoundException;
import roomescape.auth.exception.CookieNullException;

public class CookieUtil {
    private static final String ACCESS_TOKEN = "token";
    private static final long MAX_AGE_FOR_LOGIN = 60 * 50;
    private static final long MAX_AGE_FOR_LOGOUT = 0;

    public static ResponseCookie createCookieForLogin(String token) {
        return createCookie(token, MAX_AGE_FOR_LOGIN);
    }

    public static ResponseCookie createCookieForLogout() {
        return createCookie("", MAX_AGE_FOR_LOGOUT);
    }

    private static ResponseCookie createCookie(String token, long maxAge) {
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
                .orElseThrow(CookieNotFoundException::new);
    }

}
