package roomescape.dao;

import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;

@Repository
public interface MemberDao {

    Member create(Member member);
}
