package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberRepository;
import roomescape.domain.Member;
import roomescape.infrastructure.TokenExtractor;
import roomescape.infrastructure.TokenProvider;
import roomescape.infrastructure.TokenValidator;
import roomescape.service.dto.TokenRequest;
import roomescape.service.dto.TokenResponse;

@Service
public class AuthService {
    public static final String COOKIE_NAME = "token";
    private static final int COOKIE_MAX_AGE = 3600;

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final TokenExtractor tokenExtractor;
    private final TokenValidator tokenValidator;

    public AuthService(MemberRepository memberRepository, TokenProvider tokenProvider, TokenExtractor tokenExtractor,
                       TokenValidator tokenValidator) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.tokenExtractor = tokenExtractor;
        this.tokenValidator = tokenValidator;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.email()).get();
        String accessToken = tokenProvider.createToken(member);
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

    public TokenResponse extractTokenByCookies(Cookie[] cookies) {
        String token = tokenExtractor.extractByCookies(cookies);
        return new TokenResponse(token);
    }

    public String extractMemberIdByToken(TokenResponse tokenResponse) {
        return tokenExtractor.extractMemberIdByToken(tokenResponse.token());
    }

    public void isTokenValid(TokenResponse tokenResponse) {
        tokenValidator.isTokenValid(tokenResponse.token());
    }
}
