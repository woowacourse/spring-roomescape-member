package roomescape.member.dao;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberDao {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Member create(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    List<Member> findAll();
}
