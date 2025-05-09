package roomescape.user.application;

import org.springframework.stereotype.Service;
import roomescape.user.dto.LoginRequest;
import roomescape.user.infrastructure.TokenProvider;

@Service
public class AuthService {
    private final TokenProvider tokenProvider;

    public AuthService(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public String createToken(String payload) {
        return tokenProvider.createToken(payload);
    }

    public String getPayload(String token) {
        validateToken(token);
        return tokenProvider.getPayload(token);
    }

    private void validateToken(String token) {
        if(tokenProvider.isInvalidToken(token)) {
            throw new AuthorizationException();
        }
    }
}
