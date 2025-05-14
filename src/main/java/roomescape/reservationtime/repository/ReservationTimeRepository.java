package roomescape.reservationtime.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();

    ReservationTime save(ReservationTime reservationTime);

    boolean deleteById(long id);

    Optional<ReservationTime> findById(long id);

    boolean checkExistsByStartAt(LocalTime time);
}
