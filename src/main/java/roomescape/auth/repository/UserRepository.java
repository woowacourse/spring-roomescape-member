package roomescape.auth.repository;

import roomescape.auth.domain.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

}
