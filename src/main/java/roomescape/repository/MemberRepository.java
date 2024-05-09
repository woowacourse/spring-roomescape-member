package roomescape.repository;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    Member save(Member member);
}
