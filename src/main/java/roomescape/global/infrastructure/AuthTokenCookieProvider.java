package roomescape.global.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
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

    public ResponseCookie generate(String token) {
        return ResponseCookie.from(TOKEN_NAME, token)
                .httpOnly(true)
                .maxAge((int) expireMillTime / 1000)
                .path("/")
                .build();
    }

    public ResponseCookie generateExpired() {
        return ResponseCookie.from(TOKEN_NAME)
                .maxAge(0)
                .build();
    }
}
