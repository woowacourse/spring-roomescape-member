package roomescape.member.dao;

import java.util.Optional;
import roomescape.member.Member;

public interface MemberDao {
    Optional<Member> findMember(String payload);

    Optional<Member> findById(Long id);
}
