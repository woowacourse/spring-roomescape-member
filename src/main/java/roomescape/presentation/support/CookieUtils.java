package roomescape.presentation.support;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import roomescape.common.exception.UnauthorizedException;

@Component
public class CookieUtils {

    public static final String COOKIE_NAME_FOR_TOKEN = "token";

    public void setCookieForToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME_FOR_TOKEN, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String getToken(HttpServletRequest request) {
        Cookie foundCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME_FOR_TOKEN))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("권한이 필요한 접근입니다."));

        return foundCookie.getValue();
    }
}
