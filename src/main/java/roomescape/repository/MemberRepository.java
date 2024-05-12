package roomescape.repository;

import java.util.List;
import roomescape.domain.Member;

public interface MemberRepository {

    List<Member> findAll();

    Member findById(Long id);

    Member findByEmail(String email);

    Member findByEmailAndPassword(String email, String password);
}
