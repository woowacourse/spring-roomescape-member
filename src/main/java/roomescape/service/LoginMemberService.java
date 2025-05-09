package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.LoginMember;
import roomescape.exception.InvalidAuthorizationException;
import roomescape.repository.MemberRepository;
import roomescape.util.JwtTokenProvider;

@Service
public class LoginMemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginMember findMemberByToken(String token) {
        if (token == null || token.isBlank()) throw new InvalidAuthorizationException("[ERROR] 로그인이 필요합니다.");
        if (!jwtTokenProvider.validateToken(token)) throw new InvalidAuthorizationException("[ERROR] 로그인 상태가 만료되었습니다.");
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public LoginMember findMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow();
    }
}
