package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.config.JwtTokenProvider;
import roomescape.domain.Member;
import roomescape.exception.UnauthorizedException;
import roomescape.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createTokenByMember(Member member) {
        return jwtTokenProvider.createTokenByMember(member);
    }

    public Member getMemberByToken(String token) {
        long memberId = jwtTokenProvider.getMemberIdByToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UnauthorizedException("존재하지 않는 멤버 ID입니다."));
    }
}
