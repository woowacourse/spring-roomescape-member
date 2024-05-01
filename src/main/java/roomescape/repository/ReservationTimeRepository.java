package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.model.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(final ReservationTime reservationTime);

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(final Long id);

    boolean existByStartAt(final LocalTime startAt);

    void deleteById(final Long id);
}
