package roomescape.domain.repository;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {

    Long save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findBy(String email, String password);
}
