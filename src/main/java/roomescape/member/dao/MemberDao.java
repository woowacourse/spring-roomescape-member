package roomescape.member.dao;

import java.util.List;
import java.util.Optional;
import roomescape.member.Member;

public interface MemberDao {
    Optional<Member> findMember(String email);

    Optional<Member> findById(Long id);

    List<Member> findAll();
}
