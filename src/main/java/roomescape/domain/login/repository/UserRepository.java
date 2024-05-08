package roomescape.domain.login.repository;

import java.util.Optional;
import roomescape.domain.login.domain.User;


public interface UserRepository {

    User insert(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmailAndPassword(String email, String password);
}
