package roomescape.auth.domain;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.AuthorizationException;

@Component
public class CookieTokenExtractor {

    public String extract(HttpServletRequest request) {
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader == null) {
            throw new AuthorizationException();
        }

        for (String cookie : cookieHeader.split(";")) {
            String trimmed = cookie.trim();
            if (trimmed.startsWith("accessToken=")) {
                String token = trimmed.substring("accessToken=".length());
                if (token.isBlank()) {
                    throw new AuthorizationException();
                }
                return token;
            }
        }
        throw new AuthorizationException();
    }
}
