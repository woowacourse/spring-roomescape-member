package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.User;
import roomescape.dto.request.UserCreateRequest;
import roomescape.dto.response.TokenResponse;
import roomescape.dto.response.UserResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.UserRepository;

@Service
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public LoginService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public TokenResponse createToken(UserCreateRequest userCreateRequest) {
        User user = userRepository.findByEmail(userCreateRequest.email());
        return new TokenResponse(jwtTokenProvider.createToken(user));
    }

    public UserResponse createUserResponse(Cookie[] cookies) {
        Long userId = jwtTokenProvider.getUserIdFromToken(cookies);
        User user = userRepository.findById(userId);

        return new UserResponse(user.getName());
    }
}
