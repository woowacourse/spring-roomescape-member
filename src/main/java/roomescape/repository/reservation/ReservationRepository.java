package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.reservation.Reservation;

public interface ReservationRepository {
    List<Reservation> getAllReservation();
    List<Reservation> getAllReservationByName(String name);
    Reservation addReservation(Reservation reservation);
    void deleteReservation(long id);
    boolean existsByTimeId(long timeId);
    boolean existsByThemeId(long themeId);
    boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, LocalDate date);
}
