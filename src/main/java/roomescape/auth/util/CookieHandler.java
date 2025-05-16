package roomescape.auth.util;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.http.ResponseCookie;
import roomescape.exception.custom.AuthorizationException;

public class CookieHandler {

    public static ResponseCookie createCookieFromToken(final String token) {
        return ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(60 * 60)
                .build();
    }

    public static ResponseCookie createLogoutCookie() {
        return ResponseCookie.from("token", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }

    public static String extractTokenFromCookies(final Cookie[] cookies) {
        validateCookiesExisted(cookies);

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }

    private static void validateCookiesExisted(final Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 존재하지 않습니다");
        }
    }
}
