package roomescape.auth.repository;

import roomescape.auth.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);
    User save(User user);
}
