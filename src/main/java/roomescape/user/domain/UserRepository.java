package roomescape.user.domain;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    Optional<String> findPasswordByEmail(String email);

    User save(User user);
}
