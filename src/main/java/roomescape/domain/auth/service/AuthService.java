package roomescape.domain.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.domain.auth.dto.LoginRequest;
import roomescape.domain.auth.dto.TokenResponse;
import roomescape.domain.auth.dto.UserCreateRequest;
import roomescape.domain.auth.dto.UserInfoResponse;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.exception.UserNotFoundException;
import roomescape.domain.auth.repository.UserRepository;

@Service
public class AuthService {

    private final JWTManager jwtManager;
    private final UserRepository userRepository;

    public AuthService(final JWTManager jwtManager, final UserRepository userRepository) {
        this.jwtManager = jwtManager;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserInfoResponse register(final UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new AlreadyInUseException("중복되는 이메일입니다!");
        }

        final Name name = new Name(request.name());
        final User user = User.withoutId(name, request.email(), request.password());
        final User savedUser = userRepository.save(user);

        return UserInfoResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public TokenResponse createToken(final LoginRequest loginRequest) {
        final User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UserNotFoundException("해당 계정이 존재하지 않습니다."));

        final String token = jwtManager.createToken(user.getId());

        return new TokenResponse(token);
    }

    public UserInfoResponse getUserInfo(final String token) {
        final Long userId = jwtManager.parseUserId(token);

        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 계정이 존재하지 않습니다."));

        return UserInfoResponse.from(user);
    }
}
