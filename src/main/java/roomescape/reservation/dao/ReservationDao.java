package roomescape.reservation.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

public interface ReservationDao {
    List<Reservation> findAll();

    List<Reservation> findAll(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);

    Long create(Reservation reservation);

    void delete(Long id);

    Optional<Reservation> findByTimeId(Long id);

    Optional<Reservation> findById(Long id);

    Optional<Reservation> findByDateTime(LocalDate date, LocalTime time);

    List<ReservationTime> findAvailableTimesByDateAndThemeId(LocalDate date, Long themeId);

    List<Theme> findTop10Themes(LocalDate currentDate);
}

