package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import roomescape.auth.application.exception.InvalidTokenException;

public class BearerAuthorizationExtractor implements AuthorizationExtractor<String> {
    private final String TOKEN = "token";

    @Override
    public String extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return extractTokenFromCookie(cookies);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new InvalidTokenException(HttpStatus.UNAUTHORIZED, "유효한 인증 정보가 존재하지 않습니다.");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN)) {
                return cookie.getValue();
            }
        }
        throw new InvalidTokenException(HttpStatus.UNAUTHORIZED, "유효한 인증 정보가 존재하지 않습니다.");
    }
}
