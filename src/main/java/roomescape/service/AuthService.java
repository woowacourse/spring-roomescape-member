package roomescape.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenExtractor;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.infrastructure.JwtTokenValidator;
import roomescape.service.dto.TokenRequest;
import roomescape.service.dto.TokenResponse;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final JwtTokenValidator jwtTokenValidator;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider, JwtTokenExtractor jwtTokenExtractor,
                       JwtTokenValidator jwtTokenValidator) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenExtractor = jwtTokenExtractor;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        String accessToken = jwtTokenProvider.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }

    public Cookie createCookieByToken(TokenResponse token) {
        Cookie cookie = new Cookie("token", token.token());
        cookie.setMaxAge(3600); // 상수 분리
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie deleteCookieByToken(TokenResponse token) {
        Cookie cookie = new Cookie("token", token.token());
        cookie.setMaxAge(0);
        return cookie;
    }

    public TokenResponse extractTokenByCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = jwtTokenExtractor.extractByCookies(cookies);
        return new TokenResponse(token);
    }

    public String extractEmailByToken(TokenResponse tokenResponse) {
        return jwtTokenExtractor.extractEmailByToken(tokenResponse.token());
    }

    public void isTokenValid(TokenResponse tokenResponse) {
        jwtTokenValidator.isTokenValid(tokenResponse.token());
    }
}
