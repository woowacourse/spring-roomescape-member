package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.LoginRequest;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class AuthService {

    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "password1!";

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(LoginRequest loginRequest) {
        return jwtTokenProvider.createToken(loginRequest.email());
    }
}
