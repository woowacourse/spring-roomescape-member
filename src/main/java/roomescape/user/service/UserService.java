package roomescape.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        User user = new User(request.name(), DEFAULT);
        Long id = userRepository.create(user);
        return UserResponse.from(new User(id, request.name(), DEFAULT));
    }

    @Transactional
    public User findOrCreateByName(String name) {
        return userRepository.findByName(name)
                .orElseGet(() -> {
                    User newUser = new User(name, DEFAULT);
                    Long newId = userRepository.create(newUser);
                    return new User(newId, name, DEFAULT);
                });
    }
}
