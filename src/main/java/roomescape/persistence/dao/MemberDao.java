package roomescape.persistence.dao;

import java.util.List;
import java.util.Optional;
import roomescape.business.domain.Member;

public interface MemberDao {

    Member insert(Member member);

    List<Member> findAll();

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmailAndPassword(String email, String password);

    boolean existsById(Long memberId);
}
