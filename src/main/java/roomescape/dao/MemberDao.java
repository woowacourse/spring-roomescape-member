package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.member.LoginMember;

public interface MemberDao {

    List<LoginMember> findAll();

    Optional<LoginMember> findById(long id);

    Optional<LoginMember> findByEmail(String email);
}
