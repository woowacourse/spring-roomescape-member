package roomescape.domain.repostiory;

import roomescape.domain.Member;
import roomescape.exception.InvalidReservationException;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    default Member getByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new InvalidReservationException());
    }
}
