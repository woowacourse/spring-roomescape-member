package roomescape.dao.member;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberDao {
    Optional<Member> findByEmailAndPassword(String email, String password);
}
