package roomescape.dao;

import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberDao {

    Optional<Member> readById(Long id);

    Optional<Member> readByEmailAndPassword(String email, String password);
}
