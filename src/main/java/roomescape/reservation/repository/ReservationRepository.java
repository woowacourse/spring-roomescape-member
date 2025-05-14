package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    boolean deleteById(Long id);

    boolean existsByTimeId(Long id);

    boolean existsByThemeId(Long id);

    boolean existsByDateAndTimeId(final LocalDate date, final long timeId);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    List<Reservation> searchByFilters(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);
}
