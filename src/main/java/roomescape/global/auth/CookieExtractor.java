package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import roomescape.global.exception.RoomEscapeException.AuthenticationException;

public class CookieExtractor {

    public static String extractValue(Cookie[] cookies, String key) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        throw new AuthenticationException("쿠키를 추출할 수 없습니다.");
    }
}
