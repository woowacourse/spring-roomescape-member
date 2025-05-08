package roomescape.common.authorization;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.common.exception.AuthorizationException;

public class TokenAuthorizationHandler implements AuthorizationHandler<String> {
    public static final String INVALID_TOKEN_EXCEPTION_MESSAGE = "토큰 값이 존재하지 않습니다";

    @Override
    public String extractToken(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AUTHORIZATION)) {
                return cookie.getValue();
            }
        }
        throw new AuthorizationException(INVALID_TOKEN_EXCEPTION_MESSAGE);
    }

    @Override
    public void createCookie(String tokenRequestContent, HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie("token", tokenRequestContent);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    @Override
    public void deleteCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            httpServletResponse.addCookie(cookie);
        }
    }
}
