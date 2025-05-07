package roomescape.domain.repository;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);
}
