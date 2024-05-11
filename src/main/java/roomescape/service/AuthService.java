package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public String createToken(LoginRequest loginRequest) {
        return jwtTokenProvider.createToken(loginRequest.email());
    }

    public MemberResponse findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        Member member = memberRepository.findByEmail(payload);
        return MemberResponse.from(member);
    }
}
