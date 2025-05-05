package roomescape.business.model.repository;

import roomescape.business.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    List<User> findAll();

    Optional<User> findByEmail(String email);

    boolean existByEmail(String email);
}
