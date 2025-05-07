package roomescape.repository;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {
    Member save(Member member);

    boolean existsByEmail(Member member);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
