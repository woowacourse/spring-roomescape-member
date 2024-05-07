package roomescape.core.repository;

import roomescape.core.domain.Member;

public interface MemberRepository {
    Member findByEmailAndPassword(final String email, final String password);

    Member findByEmail(final String email);
}
