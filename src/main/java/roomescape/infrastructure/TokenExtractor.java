package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class TokenExtractor {

    private static final String NAME = "token";

    private TokenExtractor() {
    }

    public static String extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
    }
}
