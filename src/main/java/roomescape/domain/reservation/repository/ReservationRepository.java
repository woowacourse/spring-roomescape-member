package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.entity.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findReservations(Long themeId, Long userId, LocalDate dateFrom, LocalDate dateTo);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    Optional<Reservation> findById(Long id);

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existsByDateAndTimeId(LocalDate date, Long timeId);

    boolean existsByTimeId(Long timeId);

    boolean existsByThemeId(Long themeId);
}
