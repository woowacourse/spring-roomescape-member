package roomescape.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;

@Repository
public interface ReservationDao {

    List<Reservation> readAll();

    Reservation create(Reservation reservation);

    Boolean exist(long id);

    Boolean exist(ReservationDate reservationDate, ReservationTime reservationTime);

    void delete(long id);

    boolean existByTimeId(Long timeId);
}
