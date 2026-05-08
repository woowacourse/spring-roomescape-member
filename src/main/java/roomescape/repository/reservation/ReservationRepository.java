package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTheme.ReservationTheme;

public interface ReservationRepository {
    List<Reservation> getAllReservation(String name);
    Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, ReservationTheme theme);
    void deleteReservation(long id);
    boolean existsByTimeId(long timeId);
    boolean existsByThemeId(long themeId);
    boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, LocalDate date);
}
