package roomescape.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.auth.LoginRequest;
import roomescape.entity.member.Member;
import roomescape.exceptions.auth.AuthorizationException;
import roomescape.infrastructure.jwt.JwtTokenProvider;
import roomescape.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthenticationService(JwtTokenProvider jwtTokenProvider,
                                 MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }


    public String login(LoginRequest request) {
        String email = request.email();
        String password = request.password();
        if (!memberRepository.existsByEmailAndPassword(email, password)) {
            throw new AuthorizationException("회원 정보를 찾을 수 없습니다.");
        }
        Member member = memberRepository.findByEmail(email);
        return jwtTokenProvider.createToken(member);
    }
}
