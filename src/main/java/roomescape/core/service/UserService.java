package roomescape.core.service;

import org.springframework.stereotype.Service;
import roomescape.core.config.TokenProvider;
import roomescape.core.domain.User;
import roomescape.core.dto.auth.TokenRequest;
import roomescape.core.dto.auth.TokenResponse;
import roomescape.core.dto.user.UserResponse;
import roomescape.core.repository.UserRepository;

@Service
public class UserService {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public UserService(final TokenProvider tokenProvider, final UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    public TokenResponse createToken(final TokenRequest request) {
        final User user = userRepository.findByEmailAndPassword(request.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("올바르지 않은 이메일 또는 비밀번호입니다.");
        }
        return new TokenResponse(tokenProvider.createToken(user.getEmail()));
    }

    public UserResponse findMemberByToken(final String token) {
        final String email = tokenProvider.getPayload(token);
        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        return new UserResponse(user.getName());
    }
}
