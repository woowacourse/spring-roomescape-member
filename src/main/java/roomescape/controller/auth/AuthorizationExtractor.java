package roomescape.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AuthorizationExtractor {

    private static final String TOKEN_KEY = "token";

    public String extractToken(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_KEY))
                .findFirst()
                .get()
                .getValue();
    }
}
