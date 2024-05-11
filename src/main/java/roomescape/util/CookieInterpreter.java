package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class CookieInterpreter {

    public static Cookie cookieBuild(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public static String cookieExtract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("token")).findFirst().orElseThrow(() -> new IllegalStateException("토큰이 존재하지 않습니다."));
        return cookie.getValue();
    }
}
