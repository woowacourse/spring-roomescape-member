package roomescape.domain.repository;

import roomescape.domain.User;

public interface UserRepository {
    User save(User user);

    User findById(Long id);
}
