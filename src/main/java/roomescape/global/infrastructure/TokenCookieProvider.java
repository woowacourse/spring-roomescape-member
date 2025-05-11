package roomescape.global.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.global.exception.unauthorized.MemberUnauthorizedException;

@Component
public class TokenCookieProvider {
    public String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new MemberUnauthorizedException();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new MemberUnauthorizedException();
    }
}
