package roomescape.member.repository;

import roomescape.member.model.Member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(Long memberId);

    Member save(Member member);
}
