package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public class CookieParser {

    public static Optional<String> getCookie(final HttpServletRequest request, String target) {
        if (request == null || target == null) {
            throw new IllegalArgumentException("유효하지 않은 입력입니다");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (target.equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}
