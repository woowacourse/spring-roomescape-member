package roomescape.user.repository;

import java.util.Optional;
import roomescape.user.domain.User;

public interface UserDao {
    Optional<User> findByEmailAndPassword(String email, String password);
}
