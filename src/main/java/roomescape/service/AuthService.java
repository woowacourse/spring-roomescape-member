package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.exception.exception.InvalidLoginInfoException;
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
                .orElseThrow(() -> new InvalidLoginInfoException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!member.checkPassword(request.password())) {
            throw new InvalidLoginInfoException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtTokenProvider.createToken(member.getId(), member.getName(), member.getRole());
    }

    public String extractName(final String token) {
        return jwtTokenProvider.getTokenInfo(token).name();
    }
}
