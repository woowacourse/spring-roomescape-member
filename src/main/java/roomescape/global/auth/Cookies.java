package roomescape.global.auth;

import jakarta.servlet.http.Cookie;

public class Cookies {

    private Cookies() {
    }

    public static Cookie generate(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie generate(String token, int expiry) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(expiry);
        return cookie;
    }

    public static String get(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException("쿠키가 존재하지 않습니다.");
    }
}
