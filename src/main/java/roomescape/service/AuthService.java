package roomescape.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.domain.repository.MemberRepository;
import roomescape.exception.member.AuthenticationFailureException;
import roomescape.service.security.JwtProvider;
import roomescape.web.dto.request.member.LoginRequest;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    public boolean verifyPermission(String token, Set<Role> permission) {
        Role requestRole = jwtProvider.extractRole(token);
        return permission.contains(requestRole);
    }

    public String login(LoginRequest loginRequest) {
        Member findMember = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(AuthenticationFailureException::new);

        return "token=" + jwtProvider.encode(findMember);
    }

}
