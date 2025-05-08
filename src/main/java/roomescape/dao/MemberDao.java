package roomescape.dao;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberDao {

    long save(Member member);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
