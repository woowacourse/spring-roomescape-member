package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.MemberResponse;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(TokenRequest request) {
        String email = request.email();
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 이메일입니다."));

        validatePassword(findMember, request.password());

        return jwtTokenProvider.createToken(findMember);
    }

    public MemberResponse getMemberData(String token) {
        jwtTokenProvider.validateToken(token);
        Long memberId = Long.parseLong(jwtTokenProvider.getSubject(token));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return MemberResponse.from(member);
    }

    private void validatePassword(Member member, String requestPassword) {
        if (member.getPassword().equals(requestPassword)) {
            return;
        }

        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

}
