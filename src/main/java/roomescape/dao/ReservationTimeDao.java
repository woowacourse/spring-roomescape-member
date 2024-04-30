package roomescape.dao;

import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {
    List<ReservationTime> findAll();

    ReservationTime findById(long id);

    long save(ReservationTime reservationTime);

    boolean deleteById(long id);
}
