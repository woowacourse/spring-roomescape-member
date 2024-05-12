package roomescape.auth.jwt;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;

public final class JwtTokenExtractor {
    private JwtTokenExtractor() {

    }

    public static String extractTokenFromCookies(final Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            throw new SecurityException("[ERROR] 쿠키에 토큰 정보를 입력해주세요.");
        }
        return Arrays.asList(cookies)
                .stream()
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .orElseThrow(() -> new SecurityException("[ERROR] 쿠키에 토큰 정보를 입력해주세요."))
                .getValue();
    }
}
