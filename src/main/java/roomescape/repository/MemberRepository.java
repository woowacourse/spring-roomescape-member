package roomescape.repository;

import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberRepository {

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);
}
