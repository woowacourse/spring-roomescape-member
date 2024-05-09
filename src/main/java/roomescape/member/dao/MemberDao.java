package roomescape.member.dao;

import java.util.Optional;

public interface MemberDao {

    Optional<Long> findIdByEmailAndPassword(String email, String password);
}
