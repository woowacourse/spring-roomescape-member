package roomescape.domain.member.repository;

import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(long id);
}
