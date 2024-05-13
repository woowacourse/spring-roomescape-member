package roomescape.repository;

import java.util.List;
import roomescape.domain.Member;

public interface MemberRepository {
    Member save(final Member member);

    boolean hasDuplicateEmail(final String email);

    List<Member> findAllByRoleMember();

    Member findByEmail(final String email);

    Member findById(final Long memberId);
}
