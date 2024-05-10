package roomescape.service;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.User;
import roomescape.dto.request.UserCreateRequest;
import roomescape.dto.response.TokenResponse;
import roomescape.dto.response.UserResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.UserRepository;

@Service
public class UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public UserService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public TokenResponse createToken(UserCreateRequest userCreateRequest) {
        User user = userRepository.findByEmail(userCreateRequest.email());
        return new TokenResponse(jwtTokenProvider.createToken(user));
    }

    public UserResponse createUserResponse(Cookie[] cookies) {
        String token = jwtTokenProvider.extractTokenFromCookie(cookies);

        Long userId = Jwts.parser()
                .setSigningKey("spring-roomescape-member-secret-key")
                .parseClaimsJws(token)
                .getBody().get("id", Long.class);

        User user = userRepository.findById(userId);

        return new UserResponse(user.getName());
    }
}
