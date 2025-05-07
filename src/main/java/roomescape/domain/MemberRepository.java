package roomescape.domain;

import roomescape.persistence.query.CreateMemberQuery;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long id);

    Long create(CreateMemberQuery createMemberQuery);
}
