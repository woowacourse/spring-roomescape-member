package roomescape.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.service.auth.exception.TokenNotFoundException;

public class CookieAuthorizationExtractor implements AuthorizationExtractor<String> {
    private static final String COOKIE_NAME = "token";

    @Override
    public String extract(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(COOKIE_NAME)) {
                return cookie.getValue();
            }
        }

        throw new TokenNotFoundException("쿠키에 토큰 정보가 존재하지 않습니다.");
    }
}
