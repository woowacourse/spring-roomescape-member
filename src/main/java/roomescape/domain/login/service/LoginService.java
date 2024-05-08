package roomescape.domain.login.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.login.domain.User;
import roomescape.domain.login.repository.UserRepository;
import roomescape.exception.ClientIllegalArgumentException;

@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ClientIllegalArgumentException("없는 user를 조회 했습니다.");
        }
        return user.get();
    }
}
