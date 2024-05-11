package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.auth.exception.AuthorizationException;

import java.util.Arrays;

public class TokenExtractor {
    private static final String TOKEN_COOKIE_KEY = "token";

    public static String extract(HttpServletRequest servletRequest) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies == null) {
            throw new AuthorizationException("토큰이 존재하지 않습니다.");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE_KEY))
                .findAny()
                .orElseThrow(() -> new AuthorizationException("토큰이 존재하지 않습니다."))
                .getValue();
    }
}
