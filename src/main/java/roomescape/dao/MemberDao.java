package roomescape.dao;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberDao {

    long save(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
