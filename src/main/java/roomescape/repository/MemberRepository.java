package roomescape.repository;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {

    Member save(final Member member);

    boolean existsByEmail(final String email);

    Optional<Member> findByEmail(final String email);
}
