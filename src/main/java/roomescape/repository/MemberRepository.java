package roomescape.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public interface MemberRepository {

    List<Member> findAll();

    Member findByEmail(String email);

    Member findById(Long id);
}
