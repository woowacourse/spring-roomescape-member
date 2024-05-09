package roomescape.dao;

import java.util.Optional;
import roomescape.domain.LoginMember;

public interface LoginMemberRepository {
    Optional<LoginMember> findByEmail(String email);
}
