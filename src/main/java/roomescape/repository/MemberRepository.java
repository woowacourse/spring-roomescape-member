package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.model.Member;

public interface MemberRepository {

    List<Member> findAll();

    Long save(final Member member);

    Optional<Member> findById(final Long id);

    Optional<Member> findByEmail(final String email);

    Boolean removeById(final Long id);
}
