package roomescape.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.UserRepository;
import roomescape.exception.business.InvalidCreateArgumentException;
import roomescape.exception.business.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(final String name, final String email, final String password) {
        if (userRepository.existByEmail(email)) {
            throw new InvalidCreateArgumentException("중복된 이메일입니다.");
        }
        User user = User.create(name, email, password);
        userRepository.save(user);
        return user;
    }

    public User getById(final String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당하는 유저가 존재하지 않습니다."));
    }

    public User getByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당하는 유저가 존재하지 않습니다."));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
