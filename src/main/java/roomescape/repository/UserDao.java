package roomescape.repository;

import java.util.Optional;

import roomescape.model.User;

public interface UserDao {
    Optional<User> findUserByEmailAndPassword(String email, String password);
}
