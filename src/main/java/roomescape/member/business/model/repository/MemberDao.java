package roomescape.member.business.model.repository;

import java.util.List;
import java.util.Optional;
import roomescape.member.business.model.entity.Member;

public interface MemberDao {

    List<Member> findAll();

    Member save(Member member);

    int deleteById(Long id);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
