package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class CookieInterpreter {

    public static Cookie buildCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie expireCookie(HttpServletRequest request) {
        Cookie expiredCookie = findCookie(request);
        expiredCookie.setMaxAge(0);
        return expiredCookie;
    }

    public static String extractCookie(HttpServletRequest request) {
        return findCookie(request).getValue();
    }

    private static Cookie findCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies).filter(c -> c.getName().equals("token")).findFirst().orElseThrow(() -> new IllegalStateException("토큰이 존재하지 않습니다."));
    }
}
