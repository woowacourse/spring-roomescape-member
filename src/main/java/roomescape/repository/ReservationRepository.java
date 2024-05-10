package roomescape.repository;

import java.time.LocalDate;
import roomescape.domain.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    List<Reservation> findByPeriod(LocalDate startDate, LocalDate endDate);

    List<Reservation> searchReservations(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);
}
