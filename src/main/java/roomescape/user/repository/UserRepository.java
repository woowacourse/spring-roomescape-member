package roomescape.user.repository;

import java.util.Optional;
import roomescape.user.domain.dto.User;

public interface UserRepository {

    User findUserByEmailAndPassword(String email, String password);

    User save(User user);

    User findByIdOrThrow(Long id);

    Optional<User> findById(Long id);
}
