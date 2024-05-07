package roomescape.auth.application;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "1234";

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public boolean checkInvalidLogin(String principal, String credentials) {
        return !EMAIL.equals(principal) || !PASSWORD.equals(credentials);
    }
//
//    public MemberResponse findMember(String principal) {
//        return new MemberResponse(1L, principal, 10);
//    }
//
//    public MemberResponse findMemberByToken(String token) {
//        String payload = jwtTokenProvider.getPayload(token);
//        return findMember(payload);
//    }

    public String createToken(TokenRequest tokenRequest) {
        if (checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }

        return jwtTokenProvider.createToken(tokenRequest.getEmail());
    }
}
