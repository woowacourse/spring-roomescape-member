package roomescape.domain;

import roomescape.persistence.query.CreateUserQuery;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findById(Long id);

    Long create(CreateUserQuery createUserQuery);
}
