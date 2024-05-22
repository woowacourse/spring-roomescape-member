package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.dto.response.TokenResponse;
import roomescape.infrastructure.TokenGenerator;

import java.util.Arrays;

@Service
public class CookieService {
    private final TokenGenerator tokenGenerator;

    public CookieService(final TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    public Cookie createCookie(final TokenResponse tokenResponse) {
        final Cookie cookie = new Cookie(tokenGenerator.getCookieName(), tokenResponse.accessToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    public Cookie createEmptyCookie() {
        final Cookie cookie = new Cookie(tokenGenerator.getCookieName(), null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    public String extractTokenFromCookie(final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> tokenGenerator.getCookieName().equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다"));
    }
}
