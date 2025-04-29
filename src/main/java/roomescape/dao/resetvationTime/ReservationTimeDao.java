package roomescape.dao.resetvationTime;

import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    ReservationTime create(ReservationTime reservationTime);

    void delete(final long id);

    ReservationTime findById(long id);
}
