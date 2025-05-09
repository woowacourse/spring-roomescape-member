package roomescape.user.application.repository;

import java.util.Optional;
import roomescape.user.domain.User;

public interface UserRepository {

    Optional<User> findByEmail(String email);
}
