package roomescape.dao.reservation;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    Reservation create(Reservation reservation);

    void delete(long id);

    int countByTimeId(long timeId);
}
