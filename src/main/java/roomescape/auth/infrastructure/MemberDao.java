package roomescape.auth.infrastructure;

import java.util.Optional;
import roomescape.auth.domain.LoginMember;

public interface MemberDao {

    Optional<LoginMember> findByEmail(String email);
}
