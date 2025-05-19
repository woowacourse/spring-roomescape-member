package roomescape.auth.jwt;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import roomescape.common.exception.MissingTokenExcpetion;

@Component
public class JwtTokenExtractor {

    private static final String TOKEN = "token";

    public String extractTokenFromCookies(final Cookie[] cookies) {
        if (cookies == null) {
            throw new MissingTokenExcpetion("Token is missing");
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie != null && TOKEN.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new MissingTokenExcpetion("Token is missing"));
    }
}
