package roomescape.repository.member;

import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberRepository {

    long add(Member member);

    Optional<Member> findById(long id);

    Optional<Member> findByEmailAndPassword(String email, String password);

    boolean existsByUsernameAndPassword(String email, String password);
}
