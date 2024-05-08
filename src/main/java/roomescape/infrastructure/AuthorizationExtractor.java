package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;

public class AuthorizationExtractor {

    public String extractTokenFromCookie(final Cookie[] cookies) {
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
