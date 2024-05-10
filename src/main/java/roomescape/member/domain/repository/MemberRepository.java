package roomescape.member.domain.repository;

import roomescape.member.domain.Member;
import roomescape.exception.InvalidReservationException;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findByEmail(String email);

    default Member getByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new InvalidReservationException());
    }
}
