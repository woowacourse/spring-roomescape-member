package roomescape.service.auth;

import org.springframework.stereotype.Service;
import roomescape.domain.user.User;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.UserResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.service.user.UserService;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public String createToken(final LoginRequest loginRequest) {
        return jwtTokenProvider.createToken(loginRequest.email());
    }

    public UserResponse findUserByToken(final String token) {
        String email = jwtTokenProvider.getPayload(token);
        User user = userService.findByEmail(email);
        return new UserResponse(user.getNameValue());
    }
}
