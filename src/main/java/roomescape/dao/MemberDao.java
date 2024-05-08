package roomescape.dao;

import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberPassword;

@Repository
public interface MemberDao {

    Member create(Member member);

    boolean existByEmail(MemberEmail memberEmail);

    boolean existByEmailAndMemberPassword(MemberEmail memberEmail, MemberPassword memberPassword);
}
