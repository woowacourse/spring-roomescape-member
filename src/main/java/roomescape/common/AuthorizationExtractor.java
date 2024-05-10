package roomescape.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor {
    private final String TOKEN_COOKIE_NAME = "token";

    public String extractToken(HttpServletRequest httpServletRequest) {
        return extractTokenFromCookie(httpServletRequest.getCookies());
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
