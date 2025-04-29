package roomescape.persistence;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;


public interface ReservationDao {

    List<Reservation> findAll();

    Long create(Reservation reservation);

    void deleteById(Long reservationId);

    Optional<Reservation> findById(Long reservationId);
}
