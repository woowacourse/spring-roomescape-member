package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Reservation> findByThemeAndDate(Long themeId, LocalDate date);

    void deleteById(Long id);

    boolean existsByTimeId(Long timeId);
}
