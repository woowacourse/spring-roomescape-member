package roomescape.user.service;

import org.springframework.stereotype.Service;
import roomescape.common.vo.Role;
import roomescape.user.dto.SignUpRequest;
import roomescape.user.dto.SignUpResponse;
import roomescape.user.entity.User;
import roomescape.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SignUpResponse registerUser(SignUpRequest request) {
        User user = User.withoutId(request.getEmail(), request.getPassword(), request.getName(), Role.MEMBER);
        User registeredUser = userRepository.save(user);
        return SignUpResponse.from(registeredUser);
    }

    public User findNameByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
