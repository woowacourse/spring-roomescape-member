package roomescape.repository.member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {
    boolean existsByEmail(String email);

    Member save(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(long id);

    List<Member> findAll();
}
