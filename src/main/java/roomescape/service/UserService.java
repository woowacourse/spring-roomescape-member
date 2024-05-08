package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.User;
import roomescape.domain.UserRepository;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.ExceptionCode;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }
}
