package roomescape.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import roomescape.dao.LoginMemberRepository;
import roomescape.domain.LoginMember;
import roomescape.infrastructure.JwtTokenExtractor;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.infrastructure.JwtTokenValidator;
import roomescape.service.dto.TokenRequest;
import roomescape.service.dto.TokenResponse;

@Service
public class AuthService {
    public static final String COOKIE_NAME = "token";
    private static final int COOKIE_MAX_AGE = 3600;

    private final LoginMemberRepository loginMemberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final JwtTokenValidator jwtTokenValidator;

    public AuthService(LoginMemberRepository loginMemberRepository, JwtTokenProvider jwtTokenProvider,
                       JwtTokenExtractor jwtTokenExtractor, JwtTokenValidator jwtTokenValidator) {
        this.loginMemberRepository = loginMemberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenExtractor = jwtTokenExtractor;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        LoginMember loginMember = loginMemberRepository.findByEmail(tokenRequest.email()).get();
        String accessToken = jwtTokenProvider.createToken(loginMember);
        return new TokenResponse(accessToken);
    }

    public Cookie createCookieByToken(TokenResponse token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token.token());
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie deleteCookieByToken(TokenResponse token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token.token());
        cookie.setMaxAge(0);
        return cookie;
    }

    public TokenResponse extractTokenByCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = jwtTokenExtractor.extractByCookies(cookies);
        return new TokenResponse(token);
    }

    public String extractMemberIdByToken(TokenResponse tokenResponse) {
        return jwtTokenExtractor.extractMemberIdByToken(tokenResponse.token());
    }

    public void isTokenValid(TokenResponse tokenResponse) {
        jwtTokenValidator.isTokenValid(tokenResponse.token());
    }
}
