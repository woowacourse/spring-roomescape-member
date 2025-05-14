package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.model.Member;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long memberId);

    List<Member> findAll();

    Member saveMember(Member member);
}
