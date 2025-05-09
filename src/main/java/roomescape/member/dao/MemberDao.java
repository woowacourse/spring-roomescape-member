package roomescape.member.dao;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberDao {

    List<Member> findAll();

    Optional<Member> findById(long id);

    Optional<Member> findByEmail(String email);
}
