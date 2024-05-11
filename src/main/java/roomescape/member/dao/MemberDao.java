package roomescape.member.dao;

import java.util.List;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRegistrationInfo;

public interface MemberDao {

    List<Member> findAll();

    MemberRegistrationInfo findRegistrationInfoByEmail(String email);

    Member save(Member member);

    void delete(long id);

}
