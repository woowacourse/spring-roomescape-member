package roomescape.domain.auth.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.auth.entity.User;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
