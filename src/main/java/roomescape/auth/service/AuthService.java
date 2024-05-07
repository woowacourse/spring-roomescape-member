package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.controller.dto.LoginRequest;
import roomescape.auth.controller.dto.MemberResponse;
import roomescape.auth.controller.dto.TokenResponse;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public void authenticate(LoginRequest loginRequest) {
        if (!memberRepository.existsBy(loginRequest.email(), loginRequest.password())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        return new TokenResponse(tokenProvider.createAccessToken(loginRequest.email()));
    }

    public MemberResponse fetchByToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.");
        }
        Member member = memberRepository.findBy(tokenProvider.getPayload(token))
                .orElseThrow();
        return new MemberResponse(member.getName());
    }
}
