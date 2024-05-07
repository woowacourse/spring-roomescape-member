package roomescape.core.repository;

import roomescape.core.domain.User;

public interface UserRepository {
    User findByEmailAndPassword(String email);

    User findByEmail(String email);
}
