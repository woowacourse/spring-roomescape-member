package roomescape.member.dao;

import java.util.List;
import java.util.Optional;

public interface MemberDao {

    Optional<Long> findIdByEmailAndPassword(String email, String password);

    Optional<String> findNameById(long id);

    List<String> findAllNames();
}
