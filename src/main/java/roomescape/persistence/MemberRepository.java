package roomescape.persistence;

import java.util.Optional;
import roomescape.business.Member;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Long add(Member member);
}
