package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;

public interface ReservationDao {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(long reservationId);

    List<Reservation> findAllReservationOrderByDateAndTimeStartAt();

    List<Reservation> findAllByThemeIdAndDate(long themeId, LocalDate date);

    List<Theme> findThemeByDateOrderByThemeIdCount(LocalDate startDate, LocalDate endDate);

    void deleteById(long reservationId);

    int countByTimeId(long timeId);
}
