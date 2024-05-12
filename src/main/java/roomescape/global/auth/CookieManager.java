package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.global.exception.RoomEscapeException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static roomescape.global.exception.ExceptionMessage.*;

public class CookieManager {

    private static final String AUTH_COOKIE_NAME = "token";

    public List<Cookie> extractCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            throw new RoomEscapeException(NOT_FOUND, COOKIE_NOT_FOUND.getMessage());
        }
        return List.of(cookies);
    }

    public String extractToken(List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new RoomEscapeException(UNAUTHORIZED, TOKEN_NOT_FOUND.getMessage());
    }

    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie(AUTH_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie expireAuthCookie(List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setMaxAge(0);
                return cookie;
            }
        }
        throw new RoomEscapeException(UNAUTHORIZED, AUTHENTICATION_NOT_FOUND.getMessage());
    }
}
