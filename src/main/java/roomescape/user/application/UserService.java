package roomescape.user.application;

import org.springframework.stereotype.Service;
import roomescape.user.domain.User;
import roomescape.user.dto.LoginRequest;
import roomescape.user.infrastructure.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long findIdByEmailAndPassword(LoginRequest request) {
        return userRepository.findIdByEmailAndPassword(request.email(), request.password())
                .orElseThrow(AuthorizationException::new);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }
}
