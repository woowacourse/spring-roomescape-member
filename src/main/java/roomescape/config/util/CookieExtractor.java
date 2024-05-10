package roomescape.config.util;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import roomescape.service.exception.AuthorizationException;

public class CookieExtractor {

    public CookieExtractor() {
    }

    public static String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthorizationException("토큰이 없습니다."));
    }
}
