package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.exception.auth.UnauthorizedEmailException;
import roomescape.exception.auth.UnauthorizedPasswordException;
import roomescape.exception.auth.UnauthorizedTokenException;
import roomescape.service.dto.LoginCheckResponse;
import roomescape.service.dto.LoginRequest;
import roomescape.service.helper.JwtTokenProvider;

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

    public LoginCheckResponse loginCheck(String token) {
        String email = jwtTokenProvider.getPayload(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UnauthorizedTokenException::new);
        return new LoginCheckResponse(member);
    }
}
