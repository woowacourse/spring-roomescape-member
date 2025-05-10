package roomescape.member.infrastructure.db;

import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberDao {

    Optional<Member> selectByEmailAndPassword(String email, String password);
}
