package roomescape.service.security;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.exception.AuthorizationException;
import roomescape.infrastructure.JwtTokenHandler;
import roomescape.repository.member.MemberCredentialRepository;
import roomescape.service.dto.login.LoginRequest;

@Service
public class AuthService {

    private final MemberCredentialRepository memberCredentialRepository;
    private final JwtTokenHandler jwtTokenHandler;

    public AuthService(MemberCredentialRepository memberCredentialRepository, JwtTokenHandler jwtTokenHandler) {
        this.memberCredentialRepository = memberCredentialRepository;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    public String createToken(LoginRequest loginRequest) {
        Member member = memberCredentialRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new AuthorizationException("유효하지 않은 id/pw 입니다."));

        return jwtTokenHandler.createToken(member);
    }

    public Member findMemberByValidToken(String token) {
        return jwtTokenHandler.getMember(token);
    }
}
