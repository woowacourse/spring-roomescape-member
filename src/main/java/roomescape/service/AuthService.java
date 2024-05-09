package roomescape.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenExtractor;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.service.dto.TokenRequest;
import roomescape.service.dto.TokenResponse;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider, JwtTokenExtractor jwtTokenExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenExtractor = jwtTokenExtractor;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        String accessToken = jwtTokenProvider.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }

    public Cookie createCookieByToken(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
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
}
