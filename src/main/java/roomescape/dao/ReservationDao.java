package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAllReservations();

    Reservation addReservation(Reservation reservation);

    void removeReservationById(Long id);

    boolean existReservationByDateAndTime(LocalDate date, Long timeId);
}
