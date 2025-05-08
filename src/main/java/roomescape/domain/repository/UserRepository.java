package roomescape.domain.repository;

import java.util.Optional;
import roomescape.domain.User;

public interface UserRepository {

    Optional<User> findById(long id);

    long save(User user);

    Optional<User> findByEmail(String email);
}
