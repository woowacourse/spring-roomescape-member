package roomescape.repository.member;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {

    List<Member> findAll();

    Optional<Member> findById(long id);

    Optional<Member> findByEmail(String email);
}
