package roomescape.reservation.domain.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Member;

public interface MemberRepository {
    Long save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    List<Member> findAll();
}
