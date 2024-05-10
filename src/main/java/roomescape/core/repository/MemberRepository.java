package roomescape.core.repository;

import roomescape.core.domain.Member;

public interface MemberRepository {
    Long save(final Member member);

    boolean hasDuplicateEmail(final String email);

    Member findByEmail(final String email);
}
