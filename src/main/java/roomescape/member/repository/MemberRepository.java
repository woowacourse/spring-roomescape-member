package roomescape.member.repository;

import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberRepository {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Member findById(Long id);

}
