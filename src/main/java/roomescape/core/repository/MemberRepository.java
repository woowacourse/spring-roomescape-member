package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.Member;

public interface MemberRepository {
    Member findByEmailAndPassword(final String email, final String password);

    Member findByEmail(final String email);

    List<Member> findAll();

    Member findById(final Long id);
}
