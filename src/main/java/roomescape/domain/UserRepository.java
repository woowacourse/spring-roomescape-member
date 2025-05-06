package roomescape.domain;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmailAndPassword(String email, String password);
}
