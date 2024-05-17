package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.Reservation;

public interface ReservationRepository {

    Long save(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Long> findPopularThemesByReservation(int daysToStartBefore, int daysToEndBefore, int limit);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    void delete(Long id);

    Boolean existsByTimeId(Long id);

    Boolean existsByThemeId(Long id);

    Boolean existsByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId);

    List<Reservation> findByConditions(Long themeId, Long timeId, LocalDate dateFrom, LocalDate dateTo);
}
