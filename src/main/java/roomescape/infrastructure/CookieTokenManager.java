package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class CookieTokenManager implements TokenManager {

    private static final String TOKEN_NAME = "token";

    @Override
    public String getToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void setToken(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_NAME, token);
        addCookieToResponse(response, cookie);
    }

    @Override
    public void expireToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_NAME, null);
        cookie.setMaxAge(0);
        addCookieToResponse(response, cookie);
    }

    private void addCookieToResponse(HttpServletResponse response, Cookie cookie) {
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
