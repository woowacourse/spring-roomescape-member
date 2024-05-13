package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    List<Reservation> findByPeriod(LocalDate startDate, LocalDate endDate);

    List<Reservation> searchReservations(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);

    boolean existsByDateTimeAndTheme(Reservation reservation);
}
