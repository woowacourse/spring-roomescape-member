package roomescape.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ResourceNotFoundException;
import roomescape.exception.SameNameException;
import roomescape.user.model.Role;
import roomescape.user.model.User;
import roomescape.user.repository.UserRepository;

@Service
public class UserService {

    private static final Role DEFAULT = Role.USER;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User create(String name) {
        if (userRepository.findByName(name).isPresent()) {
            throw new SameNameException("이미 존재하는 사용자 이름입니다.");
        }

        User user = new User(name, DEFAULT);
        return userRepository.create(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다."));
    }

    @Transactional
    public User getOrCreateUserByName(String name) {
        return userRepository.findByName(name)
                .orElseGet(() -> userRepository.create(new User(name, Role.USER)));
    }
}
