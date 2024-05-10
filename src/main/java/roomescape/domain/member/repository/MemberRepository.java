package roomescape.domain.member.repository;

import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberRepository {
    boolean existsByEmailAndPassword(String email, String password);

    Optional<Member> findByEmail(String email);
}
