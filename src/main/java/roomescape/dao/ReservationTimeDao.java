package roomescape.dao;

import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {
    ReservationTime create(ReservationTime reservationTime);

    ReservationTime read(Long id);

    List<ReservationTime> readAll();

    void delete(Long id);
}
