package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberDao {

    List<Member> readAll();

    Optional<Member> readById(Long id);

    Optional<Member> readByEmailAndPassword(String email, String password);

    Optional<Member> readByEmail(String email);

    Member create(Member member);
}
