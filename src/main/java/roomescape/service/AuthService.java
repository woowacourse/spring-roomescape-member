package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.exception.AuthorizationException;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberCredentialRepository;
import roomescape.service.dto.login.LoginRequest;

@Service
public class AuthService {

    private final MemberCredentialRepository memberCredentialRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberCredentialRepository memberCredentialRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberCredentialRepository = memberCredentialRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(LoginRequest loginRequest) {
        Member member = memberCredentialRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new AuthorizationException("유효하지 않은 id/pw 입니다."));

        return jwtTokenProvider.createToken(member);
    }

    public Member findMemberByToken(String token) {
        return jwtTokenProvider.getMember(token);
    }
}
