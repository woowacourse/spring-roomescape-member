package roomescape.auth.login.infrastructure.token;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class TokenExtractor {

    public static String extract(final HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findAny()
                .orElse(null);
    }
}
