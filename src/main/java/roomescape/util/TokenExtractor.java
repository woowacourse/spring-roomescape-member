package roomescape.util;

import jakarta.servlet.http.Cookie;
import java.util.Objects;
import javax.naming.AuthenticationException;

public class TokenExtractor {

    private TokenExtractor() {
    }

    // TODO: depth 줄이기
    public static String extractTokenFromCookie(Cookie[] cookies) throws AuthenticationException {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), "token")) {
                    return cookie.getValue();
                }
            }
        }
        throw new AuthenticationException("접근 권한 확인을 위한 쿠키가 없습니다.");
    }
}
