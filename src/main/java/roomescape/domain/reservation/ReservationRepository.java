package roomescape.domain.reservation;

import roomescape.domain.theme.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    List<Reservation> findByPeriod(LocalDate startDate, LocalDate endDate);

    void deleteById(Long id);

    boolean existsByThemeAndDateTime(Theme theme, ReservationDate date, ReservationTime time);

    List<Reservation> findByCondition(Long memberId, Long themeId, String from, String to);
}
