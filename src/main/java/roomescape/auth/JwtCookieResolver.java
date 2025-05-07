package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import roomescape.exception.auth.AuthenticationException;

public class JwtCookieResolver {

    public static String getTokenFromCookie(HttpServletRequest request) {
        String jwtCookieKey = JwtTokenProvider.getCookieKey();

        return Arrays.stream(request.getCookies())
                .filter((cookie) -> jwtCookieKey.equals(cookie.getName()))
                .findAny()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationException("유효하지 않은 토큰입니다"));
    }
}
