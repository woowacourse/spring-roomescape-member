package roomescape.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.entity.member.Member;
import roomescape.exceptions.auth.AuthorizationMemberNotFoundException;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthenticationService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public String createToken(String email, String password) {
        if (!memberRepository.existsByEmailAndPassword(email, password)) {
            throw new AuthorizationMemberNotFoundException("회원 정보를 찾을 수 없습니다.");
        }
        Member member = memberRepository.findByEmail(email);
        return jwtTokenProvider.createToken(member);
    }

    public String findNameByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        Member member = memberRepository.findByEmail(payload);
        return member.getName();
    }

}
