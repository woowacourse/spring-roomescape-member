package roomescape.persistence.dao;

import java.util.List;
import java.util.Optional;
import roomescape.business.domain.User;

public interface UserDao {

    Optional<User> find(Long id);

    List<User> findAll();

    Optional<User> findByPrincipal(String principal);
}
