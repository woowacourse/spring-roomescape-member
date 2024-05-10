package roomescape.domain.user.repository;

import java.util.Optional;
import roomescape.domain.user.User;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findById(long id);
}
