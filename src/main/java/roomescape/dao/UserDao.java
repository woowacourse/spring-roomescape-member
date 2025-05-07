package roomescape.dao;

import java.util.Optional;
import roomescape.model.User;

public interface UserDao {
    Optional<User> findByEmail(final String email);
}
