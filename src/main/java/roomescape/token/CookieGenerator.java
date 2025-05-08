package roomescape.token;

import jakarta.servlet.http.Cookie;

public class CookieGenerator {

    private static final String SECRET_KEY = "aabbcc11";

    private CookieGenerator() {
    }

    public static Cookie generate(String token) {
        Cookie cookie = new Cookie(token, SECRET_KEY);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
