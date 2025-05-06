package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;

public interface ReservationDao {

    List<Reservation> findAllReservation();

    long saveReservation(Reservation reservation);

    void deleteReservation(Long id);

    int countAlreadyExistReservation(Long id);

    int findByDateAndTime(ReservationDate date, Long timeId);

    Optional<Reservation> findById(Long id);
}
