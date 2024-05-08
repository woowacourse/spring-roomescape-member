package roomescape.reservationTime.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservationTime.domain.ReservationTime;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    boolean existByStartAt(LocalTime startAt);

    ReservationTime insert(ReservationTime reservationTime);

    void deleteById(Long id);
}
