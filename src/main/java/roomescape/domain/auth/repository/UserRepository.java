package roomescape.domain.auth.repository;

import java.util.Optional;
import roomescape.domain.auth.entity.User;

public interface UserRepository {

    User save(User user);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    Optional<User> findByUserId(Long userId);

    Optional<User> findByEmail(String email);
}
