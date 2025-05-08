package roomescape.persistence;

import java.util.Optional;
import roomescape.business.Member;

public interface MemberRepository {

    Long save(Member member);

    Optional<Member> findById(Long id);
}
