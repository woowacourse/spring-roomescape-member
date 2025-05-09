package roomescape.member.repository;

import roomescape.member.entity.Member;

public interface MemberRepository {

    Member save(Member member);

    Member findByEmail(String email);
}
