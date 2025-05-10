package roomescape.persistence.dao;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.User;

@Repository
public interface UserDao {

    Optional<User> findByPrincipal(String principal);
}
