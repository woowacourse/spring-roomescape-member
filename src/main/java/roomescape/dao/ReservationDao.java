package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.model.Reservation;

public interface ReservationDao {
    List<Reservation> findAll();

    Long saveReservation(Reservation reservation);

    void deleteById(Long id);

    Optional<Reservation> findByDateAndTime(Reservation reservation);
}
