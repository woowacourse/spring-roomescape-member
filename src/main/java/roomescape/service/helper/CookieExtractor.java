package roomescape.service.helper;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import roomescape.exception.auth.UnauthorizedTokenException;

@Component
public class CookieExtractor {
    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token); // TODO: session key 상수로 빼기
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie deleteCookie() {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String getToken(Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedTokenException(); // TODO: 토큰이 존재하지 않습니다 란 예외로 따로 처리할지 고민하기
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(UnauthorizedTokenException::new);
    }
}
