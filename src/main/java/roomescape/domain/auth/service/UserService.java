package roomescape.domain.auth.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.domain.auth.dto.UserCreateRequest;
import roomescape.domain.auth.dto.UserInfoResponse;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserInfoResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .filter(User::isNotAdmin)
                .map(UserInfoResponse::from)
                .toList();
    }

    @Transactional
    public UserInfoResponse register(final UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new AlreadyInUseException("중복되는 이메일입니다!");
        }

        final Name name = new Name(request.name());
        final User user = User.withoutId(name, request.email(), request.password(), Roles.USER);
        final User savedUser = userRepository.save(user);

        return UserInfoResponse.from(savedUser);
    }
}
