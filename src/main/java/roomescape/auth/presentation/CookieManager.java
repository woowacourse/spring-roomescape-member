package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieManager {

    private static final String SET_COOKIE_KEY = "token";

    public void setTokenCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(SET_COOKIE_KEY, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void deleteTokenCookie(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie(SET_COOKIE_KEY, null);
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
    }
}
