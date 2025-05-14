package roomescape.auth.domain;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.AuthorizationException;

@Component
public class CookieTokenExtractor {

    public String extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthorizationException();
        }

        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                String token = cookie.getValue();
                if (token == null || token.isBlank()) {
                    throw new AuthorizationException();
                }
                return token;
            }
        }

        throw new AuthorizationException();
    }
}
