package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.exception.UnAuthorizedException;

import java.util.Arrays;

@Component
public class CookieProvider {

    public Cookie create(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");

        return cookie;
    }

    public Cookie invalidate(Cookie[] cookies) {
        Cookie invalidatedCookie = new Cookie("token", "");
        invalidatedCookie.setMaxAge(0);
        invalidatedCookie.setPath("/");
        invalidatedCookie.setSecure(true);

        return invalidatedCookie;
    }

    public String extractToken(final Cookie[] cookies) {
        if (cookies == null) {
            throw new UnAuthorizedException();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(UnAuthorizedException::new);
    }
}
