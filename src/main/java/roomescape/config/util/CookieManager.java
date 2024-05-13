package roomescape.config.util;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import roomescape.service.exception.AuthorizationException;

public class CookieManager {

    private static final String COOKIE_NAME = "token";

    public CookieManager() {
    }

    public static Cookie makeAuthCookie(String token) {
        return new Cookie(COOKIE_NAME, token);
    }

    public static Cookie cleanAuthCookie() {
        return new Cookie(COOKIE_NAME, null);
    }

    public static String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthorizationException("로그인이 필요합니다."));
    }
}
