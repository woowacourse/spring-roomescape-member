package roomescape.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.exception.InvalidTokenException;

@Component
public class CookieManager {

    public String getCookieByName(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        Cookie tokenCookie = null;

        if (cookies == null) {
            throw new InvalidTokenException();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                tokenCookie = cookie;
                break;
            }
        }

        if (tokenCookie == null) {
            throw new InvalidTokenException();
        }

        return tokenCookie.getValue();
    }
}
