package roomescape.repository.member;

import roomescape.domain.Member;

public interface MemberRepository {
    boolean existsByEmail(String email);

    Member save(Member member);
}
