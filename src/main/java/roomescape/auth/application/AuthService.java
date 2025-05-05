package roomescape.auth.application;

import static roomescape.auth.exception.AuthErrorCode.INVALID_PASSWORD;
import static roomescape.auth.exception.AuthErrorCode.INVALID_TOKEN;
import static roomescape.auth.exception.AuthErrorCode.USER_NOT_FOUND;

import org.springframework.stereotype.Service;
import roomescape.auth.domain.User;
import roomescape.auth.domain.UserRepository;
import roomescape.auth.dto.UserResponse;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.exception.AuthorizationException;
import roomescape.auth.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        User user = getUser(tokenRequest.email());

        if (!tokenRequest.password().equals(user.getPassword())) {
            throw new AuthorizationException(INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }

    public UserResponse findUserByToken(String token) {
        validateUserToken(token);
        String email = jwtTokenProvider.getPayload(token);
        User user = getUser(email);

        return new UserResponse(user.getName());
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException(USER_NOT_FOUND));
    }

    private void validateUserToken(String token) {
        if(!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException(INVALID_TOKEN);
        }
    }
}
