package roomescape.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.exception.custom.reason.auth.AuthNotExistsEmailException;
import roomescape.exception.custom.reason.auth.AuthNotValidPasswordException;
import roomescape.exception.custom.reason.auth.AuthNotValidTokenException;
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
        validateExistsMemberByEmail(loginRequest.email());

        final Member memberByEmail = memberRepository.findByEmail(loginRequest.email());
        validatePassword(loginRequest, memberByEmail);

        return jwtProvider.provideToken(loginRequest.email());
    }

    public LoginMember findLoginMemberByToken(final String token) {
        validateToken(token);
        final String email = jwtProvider.extractPayload(token);

        validateExistsMemberByEmail(email);
        final Member member = memberRepository.findByEmail(email);

        return new LoginMember(member.getId(), member.getName(), member.getEmail());
    }

    private void validateToken(final String token) {
        if (!jwtProvider.isValidToken(token)) {
            throw new AuthNotValidTokenException();
        }
    }

    private void validateExistsMemberByEmail(final String email) {
        if (!memberRepository.existsByEmail(email)) {
            throw new AuthNotExistsEmailException();
        }
    }

    private void validatePassword(final LoginRequest loginRequest, final Member memberByEmail) {
        if (!memberByEmail.matchesPassword(loginRequest.password())) {
            throw new AuthNotValidPasswordException();
        }
    }
}
