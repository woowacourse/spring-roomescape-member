package roomescape.auth.application;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.controller.login.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public boolean checkInvalidLogin(String email, String password) {
        return !memberRepository.existByEmailAndPassword(email, password);
    }

    public String createToken(TokenRequest tokenRequest) {
        if (checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }

        return jwtTokenProvider.createToken(tokenRequest.getEmail());
    }

    public LoginMember findMemberByToken(String token) {
        validateToken(token);
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    private LoginMember findMember(String principal) {
        Member member = memberRepository.findByEmail(principal)
                .orElseThrow(AuthorizationException::new);

        return LoginMember.from(member);
    }

    public void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException();
        }
    }
}
