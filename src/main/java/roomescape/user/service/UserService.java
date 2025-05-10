package roomescape.user.service;

import org.springframework.stereotype.Service;
import roomescape.user.domain.User;
import roomescape.user.domain.dto.UserRequestDto;
import roomescape.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User add(UserRequestDto userRequestDto) {
        User user = userRequestDto.toEntity();
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findByIdOrThrow(id);
    }
}
