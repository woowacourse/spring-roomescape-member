package roomescape.user.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class BearerAuthorizationExtractor implements AuthorizationExtractor<String> {
    private final String TOKEN = "token";

    @Override
    public String extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return extractTokenFromCookie(cookies);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
