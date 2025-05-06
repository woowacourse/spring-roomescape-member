package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;

public interface ReservationDao {

    List<Reservation> findAll();

    long save(Reservation reservation);

    void delete(Long id);

    int countAlreadyExistReservation(Long id);

    int countAlreadyReservationOf(ReservationDate date, Long timeId);

    Optional<Reservation> findById(Long id);
}
