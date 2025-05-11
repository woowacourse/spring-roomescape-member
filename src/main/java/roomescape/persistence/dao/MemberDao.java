package roomescape.persistence.dao;

import java.util.Optional;
import roomescape.business.domain.Member;

public interface MemberDao {

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmailAndPassword(String email, String password);

    boolean existsById(Long memberId);
}
