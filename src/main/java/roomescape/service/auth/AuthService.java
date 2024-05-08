package roomescape.service.auth;

import org.springframework.stereotype.Service;
import roomescape.dto.request.LoginRequest;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(final LoginRequest loginRequest) {
        return jwtTokenProvider.createToken(loginRequest.email());
    }
}
