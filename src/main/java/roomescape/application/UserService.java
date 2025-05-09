package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.User;
import roomescape.domain.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    public User findById(final long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id : " + id));
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }
}
