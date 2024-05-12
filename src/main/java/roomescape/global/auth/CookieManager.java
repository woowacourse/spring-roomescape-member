package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.global.exception.RoomEscapeException;

import java.util.List;

public class CookieManager {

    private static final String AUTH_COOKIE_NAME = "token";

    public List<Cookie> extractCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            throw new RoomEscapeException("[ERROR] 쿠키가 존재하지 않습니다");
        }
        return List.of(cookies);
    }

    public String extractToken(List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new RoomEscapeException("[ERROR] 토큰이 존재하지 않습니다.");
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
        throw new RoomEscapeException("[ERROR] 권한 정보가 없습니다");
    }
}
