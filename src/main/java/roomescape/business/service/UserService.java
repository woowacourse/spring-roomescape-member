package roomescape.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.UserRepository;
import roomescape.exception.business.InvalidCreateArgumentException;
import roomescape.exception.business.NotFoundException;

import java.util.List;

import static roomescape.exception.ErrorCode.EMAIL_DUPLICATED;
import static roomescape.exception.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(final String name, final String email, final String password) {
        if (userRepository.existByEmail(email)) {
            throw new InvalidCreateArgumentException(EMAIL_DUPLICATED);
        }
        User user = User.create(name, email, password);
        userRepository.save(user);
        return user;
    }

    public User getById(final String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));
    }

    public User getByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
