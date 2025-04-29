package roomescape.reservation.dao;

import java.util.List;
import roomescape.reservation.Reservation;

public interface ReservationDao {
    List<Reservation> findAll();

    Long create(Reservation reservation);

    int delete(Long id);
}

