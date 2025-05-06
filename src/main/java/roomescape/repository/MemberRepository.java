package roomescape.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public interface MemberRepository {
    Optional<Member> findByEmailAndPassword(String email, String password);
}
