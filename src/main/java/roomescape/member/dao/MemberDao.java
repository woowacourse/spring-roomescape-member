package roomescape.member.dao;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.LoginMember;

public interface MemberDao {

    List<LoginMember> findAll();

    Optional<LoginMember> findById(long id);

    Optional<LoginMember> findByEmail(String email);
}
