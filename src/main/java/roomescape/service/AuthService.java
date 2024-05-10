package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.exception.auth.UnauthorizedEmailException;
import roomescape.exception.auth.UnauthorizedPasswordException;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.service.dto.LoginRequest;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public String login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(UnauthorizedEmailException::new);
        if (!member.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedPasswordException();
        }

        return jwtTokenProvider.createToken(member.getEmail());
    }
}
