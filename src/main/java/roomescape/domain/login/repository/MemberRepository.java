package roomescape.domain.login.repository;

import java.util.Optional;
import roomescape.domain.login.domain.Member;


public interface MemberRepository {

    Member insert(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
