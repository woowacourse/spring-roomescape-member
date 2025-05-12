package roomescape.dao.member;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberDao {

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    boolean existsById(Long id);
}
