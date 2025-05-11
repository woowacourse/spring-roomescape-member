package roomescape.user.service;

import org.springframework.stereotype.Service;
import roomescape.user.exception.UserNotFoundException;
import roomescape.user.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isValidUser(String email, String password) {
        return userRepository.checkExistsByEmailAndPassword(email, password);
    }

    public void delete(long id) {
        if (!userRepository.deleteById(id)) {
            throw new UserNotFoundException("요청한 id와 일치하는 유저 정보가 없습니다.");
        }
    }
}
