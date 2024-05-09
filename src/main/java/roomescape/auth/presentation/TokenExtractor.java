package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.auth.exception.TokenNotExistException;

import java.util.Arrays;

public class TokenExtractor {
    private static final String TOKEN_COOKIE_KEY = "token";

    public static String extract(HttpServletRequest servletRequest) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies == null) {
            throw new TokenNotExistException();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE_KEY))
                .findAny()
                .orElseThrow(TokenNotExistException::new)
                .getValue();
    }
}
