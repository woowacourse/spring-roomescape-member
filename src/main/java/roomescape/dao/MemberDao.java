package roomescape.dao;

import roomescape.domain.Member;

public interface MemberDao {

    Member findMemberByEmail(String email);
}
