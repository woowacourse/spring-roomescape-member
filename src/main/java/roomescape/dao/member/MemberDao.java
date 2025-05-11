package roomescape.dao.member;

import java.util.List;
import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberDao {

    List<Member> findAll();

    Long save(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);
}
