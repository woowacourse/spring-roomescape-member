package roomescape.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

        return jwtTokenProvider.createToken(email);
    }
}
