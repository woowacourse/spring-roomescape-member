package roomescape.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor {
    private final String TOKEN_COOKIE_NAME = "token";

    public String extractToken(HttpServletRequest httpServletRequest) {
        Cookie cookie = extractTokenCookie(httpServletRequest)
                .orElseThrow(() -> new SecurityException("회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."));
        return cookie.getValue();
    }

    private Optional<Cookie> extractTokenCookie(HttpServletRequest httpServletRequest) {
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }
}
