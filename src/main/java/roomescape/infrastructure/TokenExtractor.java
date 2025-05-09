package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.exception.AuthorizationException;

@Component
public class TokenExtractor {

    public String extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthorizationException("로그인 정보가 입력되지 않았습니다.");
        }
        return extractTokenFromCookie(cookies);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new AuthorizationException("토큰이 존재하지 않습니다.");
    }
}
