package roomescape.reservationtime.dao;

import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.ReservationTime;

public interface ReservationTimeDao {
    List<ReservationTime> findAll();

    Long create(ReservationTime reservationTime);

    int delete(Long id);

    Optional<ReservationTime> findById(Long id);
}
