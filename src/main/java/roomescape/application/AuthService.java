package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.application.dto.request.TokenCreationRequest;
import roomescape.domain.user.User;
import roomescape.domain.user.repository.UserRepository;
import roomescape.infrastructure.JwtProvider;

@Service
public class AuthService {
    private static final String WRONG_EMAIL_OR_PASSWORD_MESSAGE = "등록되지 않은 이메일이거나 비밀번호가 틀렸습니다.";

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public AuthService(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    public String createToken(TokenCreationRequest request) {
        User user = getUser(request);
        validatePassword(user, request.password());
        return jwtProvider.createToken(user.getId(), user.getRole());
    }

    private User getUser(TokenCreationRequest request) {
        return userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException(WRONG_EMAIL_OR_PASSWORD_MESSAGE));
    }

    private void validatePassword(User user, String password) {
        if (!user.matchPassword(password)) {
            throw new IllegalArgumentException(WRONG_EMAIL_OR_PASSWORD_MESSAGE);
        }
    }
}
