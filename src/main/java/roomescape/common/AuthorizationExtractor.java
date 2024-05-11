package roomescape.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor {
    private final String TOKEN_COOKIE_NAME = "token";

    public String extractToken(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        checkCookieExist(cookies);

        Cookie cookie = extractTokenCookie(cookies)
                .orElseThrow(() -> new SecurityException("토큰에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."));
        return cookie.getValue();
    }

    private void checkCookieExist(final Cookie[] cookies) {
        if (cookies == null) {
            throw new SecurityException("쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }
    }

    private Optional<Cookie> extractTokenCookie(final Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }
}
