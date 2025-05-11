package roomescape.user.domain;

import roomescape.common.domain.Email;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(Email email);

    User save(User user);
}
