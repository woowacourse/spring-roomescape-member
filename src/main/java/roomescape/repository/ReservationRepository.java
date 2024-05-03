package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.reservation.Reservation;

public interface ReservationRepository {

    Long save(Reservation reservation);

    List<Reservation> findAll();

    Reservation findById(Long id);

    List<Long> findThemeReservationCountsForLastWeek(int daysToStartBefore, int daysToEndBefore, int limit);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    void delete(Long id);

    Boolean existsById(Long id);

    Boolean existsByTimeId(Long id);

    Boolean existsByThemeId(Long id);

    Boolean existsByDateTimeAndTheme(LocalDate date, Long timeId, Long ThemeId);

}
