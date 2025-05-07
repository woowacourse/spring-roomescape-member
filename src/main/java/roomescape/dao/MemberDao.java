package roomescape.dao;

import roomescape.entity.Member;

public interface MemberDao {
    boolean existsByEmailAndPassword(String email, String password);

    Member findByEmailAndPassword(String email, String password);

    Member findById(long memberId);
}
