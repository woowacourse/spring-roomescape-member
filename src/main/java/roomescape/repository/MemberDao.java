package roomescape.repository;

import java.util.List;
import roomescape.domain.Member;

public interface MemberDao {
    List<Member> findAll();

    Member findByEmail(String email);

    Member findById(Long id);
}
