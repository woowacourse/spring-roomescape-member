package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.global.auth.JwtTokenProvider;
import roomescape.domain.member.Member;
import roomescape.global.exception.AuthorizationException;
import roomescape.repository.MemberRepository;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new AuthorizationException("아이디 혹은 패스워드가 일치하지 않습니다."));

        return jwtTokenProvider.createToken(member);
    }

    public Long parseMemberId(String token) {
        return jwtTokenProvider.parseMemberId(token);
    }
}
