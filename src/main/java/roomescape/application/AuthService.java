package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.dto.auth.TokenRequest;
import roomescape.dto.auth.TokenResponse;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        // todo: validate 진행
        String accessToken = jwtTokenProvider.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }
}
