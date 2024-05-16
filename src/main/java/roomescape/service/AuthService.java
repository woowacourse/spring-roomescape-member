package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberRepository;
import roomescape.domain.Member;
import roomescape.exception.AuthorizationException;
import roomescape.infrastructure.TokenExtractor;
import roomescape.infrastructure.TokenProvider;
import roomescape.infrastructure.TokenValidator;
import roomescape.service.dto.MemberRequest;
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

    public boolean isMemberAdmin(Cookie[] cookies) {
        MemberRequest memberRequest = extractMemberByCookies(cookies);
        if (!memberRequest.role().equals("ADMIN")) {
            throw new AuthorizationException("관리자가 아닙니다.");
        }
        return true;
    }

    public MemberRequest extractMemberByCookies(Cookie[] cookies) {
        TokenResponse tokenResponse = extractTokenByCookies(cookies);
        isTokenValid(tokenResponse);
        String memberId = extractMemberIdByToken(tokenResponse);
        return findMemberById(memberId);
    }

    private MemberRequest findMemberById(String memberId) {
        return new MemberRequest(memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다.")));
    }

    public TokenResponse extractTokenByCookies(Cookie[] cookies) {
        String token = tokenExtractor.extractByCookies(cookies);
        return new TokenResponse(token);
    }

    public String extractMemberIdByToken(TokenResponse tokenResponse) {
        return tokenExtractor.extractMemberIdByToken(tokenResponse.token());
    }

    private void isTokenValid(TokenResponse tokenResponse) {
        tokenValidator.isTokenValid(tokenResponse.token());
    }
}
