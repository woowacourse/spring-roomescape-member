package roomescape.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDAO {
    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(long id);

    long insert(ReservationTime reservationTime);

    boolean existsByStartAt(LocalTime startAt);

    boolean deleteById(long id);
}
