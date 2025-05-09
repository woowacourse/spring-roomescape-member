package roomescape.dao;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberDao {
    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);
}
