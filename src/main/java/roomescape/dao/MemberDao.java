package roomescape.dao;

import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberDao {

    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
