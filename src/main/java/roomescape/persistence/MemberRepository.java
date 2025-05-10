package roomescape.persistence;

import java.util.List;
import java.util.Optional;
import roomescape.business.domain.member.Member;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    List<Member> findAll();

    boolean existsByEmail(String email);
}
