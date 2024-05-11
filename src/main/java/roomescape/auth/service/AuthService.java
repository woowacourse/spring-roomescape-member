package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.dao.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberResponse;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        if (!memberRepository.isMemberExist(loginRequest.email(), loginRequest.password())) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        String accessToken = jwtTokenProvider.createToken(loginRequest.email());
        return new TokenResponse(accessToken);
    }

    public MemberResponse findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    private MemberResponse findMember(String principal) {
        Member member = memberRepository.findByEmail(principal)
                .orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));

        return new MemberResponse(member);
    }
}
