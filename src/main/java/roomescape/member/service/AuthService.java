package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.dto.TokenRequest;
import roomescape.member.dto.TokenResponse;
import roomescape.member.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        String accessToken = jwtTokenProvider.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }
}
