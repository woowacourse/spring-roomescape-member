package roomescape.dao;

import roomescape.domain.Member;
import java.util.Optional;

public interface MemberDao {

    Member save(final Member member);

    Optional<Member> findById(final Long id);

    Optional<Member> findByEmail(final String email);
}
