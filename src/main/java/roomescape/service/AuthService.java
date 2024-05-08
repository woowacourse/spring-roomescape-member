package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.LoginResponse;
import roomescape.jwt.JwtTokenProvider;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse createToken(LoginRequest request) {
        String accessToken = jwtTokenProvider.createToken(request.getEmail());
        return new LoginResponse(accessToken);
    }
}
