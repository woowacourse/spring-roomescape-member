package roomescape.member.repository;

import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberRepository {

    boolean existsByEmailAndPassword(final String email, final String password);

    Optional<Member> findByMember(final String email, final String password);

    Member save(Member member);

    boolean existsById(Long id);
}
