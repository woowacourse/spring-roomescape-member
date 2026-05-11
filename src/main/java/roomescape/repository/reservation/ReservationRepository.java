package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationInfo;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;

public interface ReservationRepository {
    Reservation getReservation(long id);
    List<ReservationInfo> getAllReservation(String name);
    ReservationInfo addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, Theme theme);
    void deleteReservation(long id);
    boolean existsByTimeId(long timeId);
    boolean existsByThemeId(long themeId);
    boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, LocalDate date);
}
