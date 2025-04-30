package roomescape.dao;

import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;

public interface ReservationDao {

    List<Reservation> findAllReservation();

    void saveReservation(Reservation reservation);

    void deleteReservation(Long id);

    int findByTimeId(Long id);

    int findByDateAndTime(ReservationDate date, Long timeId);
}
