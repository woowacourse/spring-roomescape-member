package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;
import java.util.Optional;

public class CookieUtil {

    private static final String TOKEN_NAME = "token";

    public static Optional<String> extractTokenFromCookie(Cookie[] cookies) {
        if(cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (Objects.equals(TOKEN_NAME, cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }

        return Optional.empty();
    }

    public static String makeTokenCookie(String token) {
        return TOKEN_NAME + "=" + token;
    }

    public static void makeCookieExpired(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_NAME, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
