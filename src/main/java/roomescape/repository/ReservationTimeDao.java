package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import roomescape.service.reservation.ReservationTime;

public interface ReservationTimeDao {

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    ReservationTime findById(long id);

    boolean isExistsByTime(LocalTime reservationTime);

    boolean isNotExistsById(long id);

    void deleteById(long id);
}
