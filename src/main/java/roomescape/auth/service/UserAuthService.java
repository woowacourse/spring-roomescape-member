package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.entity.User;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.auth.service.dto.LoginResponse;

@Service
public class UserAuthService {
    private final JwtTokenProvider jwtTokenProvider;

    public UserAuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        User user = new User(1L, request.email(), request.password());
        String token = jwtTokenProvider.createToken(user);
        return new LoginResponse(token);
    }
}
