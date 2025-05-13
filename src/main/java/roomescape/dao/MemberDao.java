package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberDao {
    List<Member> findAll();

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);
}
