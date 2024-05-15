package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(long id);

    List<Member> findAll();
}
