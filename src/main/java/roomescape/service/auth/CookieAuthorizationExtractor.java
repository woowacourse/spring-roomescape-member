package roomescape.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.service.exception.TokenNotFoundException;

public class CookieAuthorizationExtractor implements AuthorizationExtractor<String> {
    private static final String COOKIE_NAME = "token";

    @Override
    public String extract(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(COOKIE_NAME)) {
                return cookie.getValue();
            }
        }

        throw new TokenNotFoundException("토큰 정보를 쿠키에서 찾을 수 없습니다.");
    }
}
