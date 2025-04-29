package roomescape.reservationtime.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(final LocalTime startAt);

    List<ReservationTime> findAll();

    void deleteById(final Long id);

    Optional<ReservationTime> findById(final Long id);
}
