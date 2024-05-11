package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class JwtTokenExtractor {

    private final HttpServletRequest request;

    public JwtTokenExtractor(HttpServletRequest request) {
        this.request = request;
    }

    public String extractTokenFromCookie() {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException("토큰을 찾지 못했습니다");
    }
}
