package roomescape.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginRequest;
import roomescape.exception.custom.reason.auth.AuthNotExistsEmailException;
import roomescape.exception.custom.reason.auth.AuthNotValidPasswordException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(
            final MemberRepository memberRepository,
            final JwtProvider jwtProvider
    ) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public String generateToken(final LoginRequest loginRequest) {
        validateExistsMemberByEmail(loginRequest);

        final Member memberByEmail = memberRepository.findByEmail(loginRequest.email());
        validatePassword(loginRequest, memberByEmail);

        return jwtProvider.provideToken(loginRequest.email());
    }

    private void validateExistsMemberByEmail(final LoginRequest loginRequest) {
        if (!memberRepository.existsByEmail(loginRequest.email())) {
            throw new AuthNotExistsEmailException();
        }
    }

    private static void validatePassword(final LoginRequest loginRequest, final Member memberByEmail) {
        if (!memberByEmail.matchesPassword(loginRequest.password())) {
            throw new AuthNotValidPasswordException();
        }
    }
}
