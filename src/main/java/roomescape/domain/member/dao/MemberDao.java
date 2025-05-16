package roomescape.domain.member.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.member.model.Member;

public interface MemberDao {

    long save(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long id);

    List<Member> findAll();
}
