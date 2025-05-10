package roomescape.infrastructure.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.exceptions.auth.AuthenticationException;

public class JwtCookieResolver {

    private static final String COOKIE_KEY = "token";

    public static String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthenticationException("로그인이 필요한 요청입니다.");
        }
        return extractTokenFromCookie(cookies);
    }

    private static String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_KEY)) {
                return cookie.getValue();
            }
        }
        throw new AuthenticationException("로그인이 필요한 요청입니다.");
    }
}
