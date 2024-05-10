package roomescape.application;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.application.dto.MemberRequest;
import roomescape.application.dto.MemberResponse;
import roomescape.domain.member.Member;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.dto.auth.TokenRequest;
import roomescape.dto.auth.TokenResponse;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        if (!memberRepository.existsByEmailAndPassword(tokenRequest.email(), tokenRequest.password())) {
            throw new IllegalArgumentException("로그인 정보가 잘못되었습니다.");
        }

        Optional<Member> member = memberRepository.findByEmail(tokenRequest.email());
        MemberRequest memberRequest = new MemberRequest(member.get().getUserName());

        String accessToken = jwtTokenProvider.createToken(memberRequest.name().getValue());
        return new TokenResponse(accessToken);
    }

    public MemberResponse findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return new MemberResponse(payload);
    }
}
