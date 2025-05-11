package roomescape.member.login.authorization;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import roomescape.common.exception.AuthorizationException;

@Component
public class TokenAuthorizationHandler implements AuthorizationHandler<String> {
    private static final String INVALID_COOKIE_EXCEPTION_MESSAGE = "쿠키가 존재하지 않습니다";
    private static final String INVALID_TOKEN_VALUE_EXCEPTION_MESSAGE = "토큰 값이 존재하지 않습니다";

    @Override
    public String extractToken(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        validateCookie(cookies);
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AUTHORIZATION)) {
                return cookie.getValue();
            }
        }
        throw new AuthorizationException(INVALID_TOKEN_VALUE_EXCEPTION_MESSAGE);
    }

    @Override
    public void createCookie(String accessToken, HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    @Override
    public void deleteCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie[] cookies = httpServletRequest.getCookies();
        validateCookie(cookies);
        for (Cookie cookie : cookies) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            httpServletResponse.addCookie(cookie);
        }
    }

    @Override
    public void validateCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthorizationException(INVALID_COOKIE_EXCEPTION_MESSAGE);
        }
    }
}
