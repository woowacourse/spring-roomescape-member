package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.InvalidPasswordException;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberRepository memberRepository, final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(final LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new DataNotFoundException("[ERROR] 이메일에 해당하는 회원을 찾을 수 없습니다."));

        if (!member.checkPassword(request.password())) {
            throw new InvalidPasswordException("[ERROR] 비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(member.getId(), member.getName(), member.getRole());
    }

    public String extractName(final String token) {
        return jwtTokenProvider.getTokenInfo(token).name();
    }
}
