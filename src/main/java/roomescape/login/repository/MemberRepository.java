package roomescape.login.repository;

import java.util.Optional;

public interface MemberRepository {

    Optional<String> findNameByEmail(final String email);

    boolean existsByEmailAndPassword(final String email, final String password);

}
