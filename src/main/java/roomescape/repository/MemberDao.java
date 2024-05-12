package roomescape.repository;

import java.util.List;
import roomescape.domain.Member;

public interface MemberDao {
    List<Member> findAll();

    Member findById(Long id);

    Member findByEmailAndPassWord(String email, String password);
}
