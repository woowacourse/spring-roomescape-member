package roomescape.domain.reservationdate;

import java.util.Optional;

public interface ReservationDateRepository {
    Optional<ReservationDate> findById(Long id);
}
