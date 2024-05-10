package roomescape.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.service.auth.exception.TokenNotFoundException;

public class CookieAuthorizationExtractor implements AuthorizationExtractor<String> {
    private static final String COOKIE_NAME = "token";

    @Override
    public String extract(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new TokenNotFoundException("토큰이 존재 하지 않습니다.");
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(COOKIE_NAME)) {
                return cookie.getValue();
            }
        }

        throw new TokenNotFoundException("토큰이 존재 하지 않습니다.");
    }
}
