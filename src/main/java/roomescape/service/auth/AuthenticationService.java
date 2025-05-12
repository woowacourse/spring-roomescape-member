package roomescape.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.Member;
import roomescape.infrastructure.jwt.JwtTokenProvider;
import roomescape.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public AuthenticationService(JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder,
                                 MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }


    public String login(String email, String password) {
        if (!memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("[ERROR] 회원 정보를 찾을 수 없습니다.");
        }
        Member member = memberRepository.findByEmail(email);
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenProvider.createToken(member);
    }
}
