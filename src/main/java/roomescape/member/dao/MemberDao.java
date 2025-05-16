package roomescape.member.dao;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberDao {

    long insert(Member member);

    boolean existsByEmail(String email);

    List<Member> findAll();

    Optional<Member> findById(long id);

    Optional<Member> findByEmail(String email);
}
