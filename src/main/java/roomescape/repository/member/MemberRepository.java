package roomescape.repository.member;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {

    Member findMemberByEmailAndPassword(Member memberRequest);

    Optional<Member> findMemberById(Long id);

    List<Member> findAllMembers();

    Long addMember(Member member);
}
