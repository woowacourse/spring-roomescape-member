package roomescape.domain.auth.repository;

import roomescape.domain.auth.entity.User;

public interface UserRepository {

    boolean existsByName(String name);

    User save(User user);

    boolean existsByEmail(String email);
}
