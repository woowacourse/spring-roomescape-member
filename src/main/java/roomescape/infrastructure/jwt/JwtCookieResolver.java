package roomescape.infrastructure.jwt;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieResolver {

    private static final String COOKIE_KEY = "token";

    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie(COOKIE_KEY, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
