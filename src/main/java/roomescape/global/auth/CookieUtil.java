package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public Cookie createCookie(String parameter, String value) {
        Cookie cookie = new Cookie(parameter, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String extractValueFromCookie(Cookie[] cookies, String parameter) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(parameter)) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
