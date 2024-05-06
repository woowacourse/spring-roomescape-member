package roomescape.member.domain.repository;

import roomescape.member.domain.Member;

public interface MemberRepository {
    Member save(Member member);
}
