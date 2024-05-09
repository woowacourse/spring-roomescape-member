package roomescape.domain;

import java.util.Optional;

public interface UserRepository {

    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
}
