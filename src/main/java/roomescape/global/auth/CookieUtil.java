package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import roomescape.global.exception.custom.UnauthorizedException;

public class CookieUtil {

    private CookieUtil() {
    }

    private static final String TOKEN_NAME = "token";

    public static Cookie setTokenInfo(final String token) {
        final Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public static String parseCookie(final Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedException();
        }
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException())
                .getValue();
    }
}
