package roomescape.member.domain.repository;

import roomescape.member.domain.Member;

public interface MemberRepository {
    Member save(Member member);

    boolean existBy(String email, String password);

    Member findByEmail(String email);

    Member findById(long memberId);
}
