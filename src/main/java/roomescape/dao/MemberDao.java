package roomescape.dao;

import roomescape.domain.Member;

public interface MemberDao {
    Member findByEmail(String email);
}
