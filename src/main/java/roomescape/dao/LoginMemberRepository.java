package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.LoginMember;

public interface LoginMemberRepository {
    Optional<LoginMember> findByEmail(String email);

    Optional<LoginMember> findById(long id);

    List<LoginMember> findAll();
}
