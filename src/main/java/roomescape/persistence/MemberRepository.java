package roomescape.persistence;

import java.util.Optional;
import roomescape.business.domain.Member;

public interface MemberRepository {

    Long save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);
}
