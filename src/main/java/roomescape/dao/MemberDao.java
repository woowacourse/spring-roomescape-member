package roomescape.dao;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberDao {

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);
}
