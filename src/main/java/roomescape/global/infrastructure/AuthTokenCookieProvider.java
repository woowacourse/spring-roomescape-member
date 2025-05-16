package roomescape.global.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.global.exception.unauthorized.MemberUnauthorizedException;

@Component
public class AuthTokenCookieProvider {

    private final String TOKEN_NAME = "token";

    @Value("${security.jwt.token.expire-length}")
    private long expireMillTime;

    public String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new MemberUnauthorizedException();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_NAME)) {
                return cookie.getValue();
            }
        }
        throw new MemberUnauthorizedException();
    }

    public Cookie generate(String token) {
        Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) expireMillTime / 1000);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie generateExpired() {
        Cookie cookie = new Cookie(TOKEN_NAME, null);
        cookie.setMaxAge(0);
        return cookie;
    }
}
