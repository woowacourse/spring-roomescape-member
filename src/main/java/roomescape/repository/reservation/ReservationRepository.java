package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationInfo;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;

public interface ReservationRepository {
    Optional<Reservation> getReservation(long id);
    List<ReservationInfo> getAllReservation(String name);
    ReservationInfo addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, Theme theme);
    void deleteReservation(long id);
    int updateAll(long id, ReservationCommand reservationCommand);
    boolean existsByTimeId(long timeId);
    boolean existsByThemeId(long themeId);
    boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, LocalDate date);
}
