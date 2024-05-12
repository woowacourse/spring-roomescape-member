package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.User;
import roomescape.dto.request.UserCreateRequest;
import roomescape.dto.response.TokenResponse;
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

    public User createUser(Cookie[] cookies) {
        Long userId = jwtTokenProvider.getUserIdFromToken(cookies);
        return userRepository.findById(userId);
    }
}
