package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.api.JwtTokenProvider;
import roomescape.controller.api.dto.response.TokenResponse;
import roomescape.domain.Member;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(final Member member) {
//        if (checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
//            throw new AuthorizationException();
//        }

        final String accessToken = jwtTokenProvider.createToken(member);
        return new TokenResponse(accessToken);
    }

    public String some(final String token) {
        return jwtTokenProvider.getPayload(token);
    }

    public boolean isAdmin(final String token) {
        return jwtTokenProvider.validateAdmin(token);
    }
}
