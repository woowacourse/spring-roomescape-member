package roomescape.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ResourceNotFoundException;
import roomescape.exception.SameNameException;
import roomescape.user.dto.UserRequest;
import roomescape.user.dto.UserResponse;
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
    public UserResponse create(UserRequest request) {
        if (userRepository.findByName(request.name()).isPresent()) {
            throw new SameNameException("이미 존재하는 사용자 이름입니다.");
        }

        User user = new User(request.name(), DEFAULT);
        User newUser = userRepository.create(user);
        return UserResponse.from(newUser);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다."));
    }

    @Transactional
    public UserResponse getOrCreateUserByName(String name) {
        User user = userRepository.findByName(name)
                .orElseGet(() -> userRepository.create(new User(name, Role.USER)));

        return UserResponse.from(user);
    }
}
