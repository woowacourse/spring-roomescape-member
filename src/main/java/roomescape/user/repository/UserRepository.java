package roomescape.user.repository;

import roomescape.user.entity.User;

public interface UserRepository {

    User save(User user);

    User findByEmail(String email);
}
