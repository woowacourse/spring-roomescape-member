package roomescape.domain.member.repository;

import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberRepository {
    Member save(Member member);

    boolean existsByEmailAndPassword(String email, String password);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);
}
