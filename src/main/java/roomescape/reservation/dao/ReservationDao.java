package roomescape.reservation.dao;

import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationDao {

    Reservation save(Reservation reservation);

    List<Reservation> findAllReservationOrderByDateAndTimeStartAt();

    void deleteById(long reservationId);

    int countByTimeId(long timeId);
}
