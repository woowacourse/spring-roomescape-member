package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.entity.User;
import roomescape.auth.repository.UserRepository;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.auth.service.dto.LoginResponse;

@Service
public class UserAuthService {
    private final UserRepository userRepository;
    private final TokenProvider jwtTokenProvider;

    public UserAuthService(UserRepository userRepository, TokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        User user = new User(1L, request.email(), request.password());
        String token = jwtTokenProvider.createToken(user);
        return new LoginResponse(token);
    }
}
