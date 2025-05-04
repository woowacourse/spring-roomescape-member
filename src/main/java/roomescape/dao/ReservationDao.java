package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAllReservations();

    boolean existReservationByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId);

    Reservation addReservation(Reservation reservation);

    void removeReservationById(Long id);
}
