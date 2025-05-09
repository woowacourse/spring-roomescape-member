package roomescape.dao;

import roomescape.domain.Member;

public interface MemberDao {

    Member findMemberByEmail(String email);

    Member findMemberById(Long id);

    boolean existMemberByEmail(String email);

    Member addMember(Member member);
}
