package roomescape.domain.member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findByEmail(String email);

    Member getByEmail(String email);

    Optional<Member> findById(long id);

    Member getById(long id);

    boolean existsByEmail(String email);

    List<Member> findAll();
}
