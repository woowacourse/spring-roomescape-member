package roomescape.global.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.UnauthorizedException;

import java.util.Arrays;

@Component
public class JwtTokenExtractor {

    private static final String JWT_COOKIE_NAME = "jwt_token";

    public String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(JWT_COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new UnauthorizedException("유효한 토큰이 존재하지 않습니다."));
    }
}
