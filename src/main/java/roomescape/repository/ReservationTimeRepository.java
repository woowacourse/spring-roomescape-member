package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);

    boolean existsByStartAt(LocalTime startAt);

    Optional<ReservationTime> findById(long id);

    List<ReservationTime> findAll();

    void delete(long id);
}
