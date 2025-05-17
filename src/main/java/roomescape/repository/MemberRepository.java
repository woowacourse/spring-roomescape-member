package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {

    Optional<Member> findById(final Long id);

    Optional<Member> findByEmail(final String email);

    List<Member> findAll();

    Member save(final Member member);

    boolean deleteById(final Long id);

}
