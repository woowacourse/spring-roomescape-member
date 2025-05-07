package roomescape.dao;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberDao {

    Member save(Member member);

    Optional<Member> findByEmail(String email);
}
