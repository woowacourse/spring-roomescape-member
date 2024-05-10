package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.domain.User;

import java.util.List;

@Repository
public interface UserRepository {

    List<User> findAll();

    User findByEmail(String email);

    User findById(Long id);

    User save(User user);

    int deleteById(Long id);
}
