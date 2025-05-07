package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import roomescape.exception.auth.AuthenticationException;

public class JwtCookieResolver {

    private static final String EMPTY_TOKEN = "EMPTY";

    //TODO 쿠키 일일이 삭제해야하는지 확인하기 수정
//    public static void resetToken(HttpServletRequest request) {
//        String jwtCookieKey = JwtTokenProvider.getCookieKey();
//        Optional<Cookie> tokenCookie = Arrays.stream(request.getCookies())
//                .filter((cookie) -> jwtCookieKey.equals(cookie.getName()))
//                .findAny();
//        tokenCookie.ifPresent(cookie -> cookie.setMaxAge(0));
//    }

    public static String getTokenFromCookie(HttpServletRequest request) {
        String jwtCookieKey = JwtTokenProvider.getCookieKey();
        
        return Arrays.stream(request.getCookies())
                .filter((cookie) -> jwtCookieKey.equals(cookie.getName()))
                .findAny()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationException("유효하지 않은 토큰입니다"));
    }
}
