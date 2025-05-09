package roomescape.dao.meber;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberDao {

    Member create(Member member);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
