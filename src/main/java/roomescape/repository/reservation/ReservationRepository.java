package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.ReservationWithTimeAndTheme;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservation.ReservationWithTime;

public interface ReservationRepository {
    Optional<ReservationWithTimeAndTheme> getReservationWithTimeAndTheme(long id);
    Optional<ReservationWithTime> getReservationWithTime(long id);
    List<ReservationWithTimeAndTheme> getAllReservation(String name);
    long addReservation(ReservationCommand reservationCommand);
    void deleteReservation(long id);
    int updateAll(long id, ReservationCommand reservationCommand);
    boolean existsByTimeId(long timeId);
    boolean existsByThemeId(long themeId);
    boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, LocalDate date);
}
