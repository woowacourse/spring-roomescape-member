package roomescape.controller.helper;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import roomescape.exception.auth.UnauthorizedTokenException;

@Component
public class CookieExtractor {
    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String getToken(Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedTokenException();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(UnauthorizedTokenException::new);
    }
}
