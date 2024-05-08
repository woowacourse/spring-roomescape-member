package roomescape.domain.user.repository;

import roomescape.domain.user.User;

public interface UserRepository {
    User save(User user);
}
