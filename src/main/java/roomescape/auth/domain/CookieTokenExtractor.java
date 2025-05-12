package roomescape.auth.domain;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class CookieTokenExtractor {

    public String extract(HttpServletRequest request) {
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader == null) {
            return null;
        }

        for (String cookie : cookieHeader.split(";")) {
            String trimmed = cookie.trim();
            if (trimmed.startsWith("accessToken=")) {
                return trimmed.substring("accessToken=".length());
            }
        }
        return null;
    }
}
