package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    Reservation add(Reservation reservation);

    int deleteById(Long id);

    List<Reservation> findAll();

    boolean existsByDateAndTime(LocalDate date, Long timeId);

    boolean existsByTimeId(Long id);

    List<Long> findTimeIdsByDateAndTheme(LocalDate date, Long themeId);

    List<Long> findThemeIdsOrderByReservationCountBetween(LocalDate startDate, LocalDate endDate, int limit);
}
