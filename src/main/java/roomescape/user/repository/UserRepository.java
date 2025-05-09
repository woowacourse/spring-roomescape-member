package roomescape.user.repository;

import java.util.Optional;
import roomescape.user.domain.User;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

}
