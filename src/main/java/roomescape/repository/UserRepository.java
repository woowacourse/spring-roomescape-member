package roomescape.repository;

import java.util.Optional;
import roomescape.domain.User;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    long save(User user);
}
