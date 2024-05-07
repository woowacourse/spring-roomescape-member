package roomescape.member.domain.repository;

import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findBy(String email);

    boolean existsBy(String email, String password);
}
