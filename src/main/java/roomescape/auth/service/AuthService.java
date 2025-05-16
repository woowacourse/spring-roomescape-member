package roomescape.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.global.exception.error.InvalidRequestException;
import roomescape.global.exception.error.NotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider,
                       PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public String createToken(TokenRequest request) {
        String email = request.email();
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자 이메일입니다."));

        validatePassword(findMember, request.password());

        return jwtTokenProvider.createToken(findMember);
    }

    private void validatePassword(Member member, String requestPassword) {
        if (passwordEncoder.matches(requestPassword, member.getPassword())) {
            return;
        }
        throw new InvalidRequestException("비밀번호가 일치하지 않습니다.");
    }

}
