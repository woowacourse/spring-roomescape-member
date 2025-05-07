package roomescape.repository;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {

    long insert(Member member);

    Optional<Member> findById(long id);

    Optional<Member> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
}
