package roomescape.business.model.repository;

import roomescape.business.model.entity.User;

import java.util.Optional;

// TODO : JDBC 구현체 작성
public interface UserRepository {

    Optional<User> findByEmail(String email);
}
