package roomescape.repository;

import roomescape.domain.Member;

public interface MemberRepository {

    Member save(final Member member);

    boolean existsByEmail(final String email);
}
