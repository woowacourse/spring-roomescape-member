package roomescape.repository;

import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberRepository {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long id);
}
