package roomescape.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import roomescape.global.exception.auth.AuthenticationException;

public class CookieUtils {

    private static final String TOKEN_NAME = "token";

    private CookieUtils() {
    }

    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthenticationException("로그인 되어 있지 않습니다");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                .map(Cookie::getValue)
                .findAny()
                .orElseThrow(() -> new AuthenticationException("로그인 되어 있지 않습니다"));
    }
}
