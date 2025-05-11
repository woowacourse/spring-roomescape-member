package roomescape.dao;

import java.util.List;
import roomescape.entity.Member;

public interface MemberDao {

    Member findById(long memberId);

    List<Member> findAll();

    Member create(Member member);

    void deleteById(long id);

    boolean existsByEmailAndPassword(String email, String password);

    boolean existsByEmail(Member member);

    Member findByEmailAndPassword(String email, String password);
}
