package roomescape.domain.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.entity.Member;

public interface MemberRepository {

    Member save(final Member member);

    boolean existsByEmail(final String email);

    Optional<Member> findByEmail(final String email);

    Optional<Member> findById(Long id);

    List<Member> findAll();
}
