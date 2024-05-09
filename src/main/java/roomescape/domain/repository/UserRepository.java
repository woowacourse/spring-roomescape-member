package roomescape.domain.repository;

import java.util.Optional;

import roomescape.domain.User;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmailAndPassword(String email, String password);

    void deleteAll();
}
