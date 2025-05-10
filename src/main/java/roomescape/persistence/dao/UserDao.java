package roomescape.persistence.dao;

import java.util.Optional;
import roomescape.business.domain.User;

public interface UserDao {

    Optional<User> findByPrincipal(String principal);
}
