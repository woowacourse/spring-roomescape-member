package roomescape.business.model.repository;

import roomescape.business.model.entity.User;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findByEmail(String email);

    boolean existByEmail(String email);
}
