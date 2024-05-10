package roomescape.util.infrastructure;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;

public class JwtTokenExtractor {
    public static String extractTokenFromCookies(final Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            return "";
        }
        return Arrays.asList(cookies)
                .stream()
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 쿠키에 토큰 정보를 입력해주세요."))
                .getValue();
    }
}
