package roomescape.user.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.user.dto.UserRequest;
import roomescape.user.dto.UserResponse;
import roomescape.user.model.Role;
import roomescape.user.model.User;
import roomescape.user.repository.UserRepository;
import roomescape.exception.ErrorCode;

@Service
public class UserService {

    private static final Role DEFAULT = Role.USER;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        try {
            User user = new User(request.name(), DEFAULT);
            Long id = userRepository.create(user);
            return UserResponse.from(new User(id, request.name(), DEFAULT));
        } catch (DuplicateKeyException e) {
            throw new ConflictException(ErrorCode.DUPLICATE_USER_NAME);
        }
    }

    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
