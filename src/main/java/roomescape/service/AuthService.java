package roomescape.service;

import roomescape.domain.user.User;
import roomescape.dto.LoginRequest;
import roomescape.dto.TokenResponse;
import roomescape.dto.UserPayload;
import roomescape.infrastructure.JwtProvider;

public class AuthService {

    private final JwtProvider jwtProvider;

    public AuthService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public TokenResponse login(LoginRequest request) {
        User loginUser = authenticateUser(request);
        UserPayload loginUserPayload = UserPayload.from(loginUser);
        String token = jwtProvider.createToken(loginUserPayload);
        return new TokenResponse(token);
    }
}
