package roomescape.web.security;

import java.util.Arrays;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class CookieTokenExtractor {

    public String extract(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
