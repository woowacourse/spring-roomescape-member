package roomescape.config;

import jakarta.servlet.http.Cookie;

import java.util.Objects;

public class CookieParser {

    private static final String TOKEN_NAME = "token";

    public static String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (Objects.equals(TOKEN_NAME, cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new TokenValidationFailureException("토큰이 존재하지 않습니다.");
    }
}
