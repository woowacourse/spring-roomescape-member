package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.Member;

public interface MemberRepository {
    Long save(final Member member);

    boolean hasDuplicateEmail(final String email);

    List<Member> findAllByRoleMember();

    Member findByEmail(final String email);

    Member findById(final Long memberId);
}
