package roomescape.user.repository;

import java.util.List;
import java.util.Optional;
import roomescape.user.domain.User;

public interface UserRepository {
    List<User> getAll();

    User put(User user);

    boolean deleteById(long id);

    Optional<User> findById(long id);

    boolean checkExistsByEmailAndPassword(String email, String password);
}
