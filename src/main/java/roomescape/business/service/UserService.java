package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.UserRepository;
import roomescape.exception.business.DuplicatedEmailException;
import roomescape.exception.business.UserNotFoundException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(final String name, final String email, final String password) {
        if (userRepository.existByEmail(email)) {
            throw new DuplicatedEmailException();
        }
        User user = User.beforeSave(name, email, password);
        userRepository.saveAndGet(user);
    }

    public User getByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
