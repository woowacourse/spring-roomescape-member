package roomescape.reservation.dao;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.Reservation;

public interface ReservationDao {
    List<Reservation> findAll();

    Long create(Reservation reservation);

    Integer delete(Long id);

    Optional<Reservation> findByTimeId(Long id);

    Optional<Reservation> findById(Long id);
}

