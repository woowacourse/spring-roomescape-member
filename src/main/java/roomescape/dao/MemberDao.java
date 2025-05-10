package roomescape.dao;

import java.util.List;
import roomescape.entity.Member;

public interface MemberDao {

    Member findById(long memberId);

    List<Member> findAll();

    boolean existsByEmailAndPassword(String email, String password);

    Member findByEmailAndPassword(String email, String password);

    Member create(Member member);
}
