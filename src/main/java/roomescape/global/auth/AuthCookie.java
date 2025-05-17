package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.global.exception.custom.UnauthorizedException;

@Component
public class AuthCookie {

    @Value("${cookie.token-name}")
    private String name;
    @Value("${jwt.expire-seconds}")
    private int maxAge;

    public Cookie createTokenCookie(final String token) {
        final Cookie cookie = new Cookie(name, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public Cookie createExpiredCookie() {
        final Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public String getValue(final Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedException();
        }
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .orElseThrow(UnauthorizedException::new)
                .getValue();
    }
}
