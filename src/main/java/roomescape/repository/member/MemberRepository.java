package roomescape.repository.member;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {
    boolean existsByEmail(String email);

    Member save(Member member);

    Optional<Member> findByEmail(String email);
}
