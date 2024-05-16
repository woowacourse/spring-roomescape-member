package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberRepository;
import roomescape.domain.Member;
import roomescape.exception.AuthorizationException;
import roomescape.service.dto.MemberRequest;
import roomescape.service.dto.TokenRequest;
import roomescape.service.dto.TokenResponse;
import roomescape.service.tokenmanager.TokenExtractor;
import roomescape.service.tokenmanager.TokenProvider;
import roomescape.service.tokenmanager.TokenValidator;

@Service
public class AuthService {
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
        Member member = findMemberByEmail(tokenRequest.email());
        String accessToken = tokenProvider.createToken(member);
        return new TokenResponse(accessToken);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));
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
        tokenValidator.validateToken(tokenResponse.token());
    }
}
