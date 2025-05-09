package roomescape.dao;

import java.util.Optional;
import roomescape.domain.member.LoginMember;

public interface MemberDao {

    Optional<LoginMember> findById(long id);

    Optional<LoginMember> findByEmail(String email);
}
