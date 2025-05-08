package roomescape.member.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.member.presentation.AuthorizationExtractor;

public class CookieAuthorizationExtractor implements AuthorizationExtractor {

    @Override
    public String extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
