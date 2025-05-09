package roomescape.global.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class CookieAuthorizationExtractor implements AuthorizationExtractor {

    @Override
    public String extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> "token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
