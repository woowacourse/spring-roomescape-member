package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Optional<Reservation> findById(Long id);

    Reservation save(Reservation reservation);

    Integer delete(Long id);

    Boolean existsByDateAndThemeAndTime(LocalDate date, Long themeId, Long timeId);

    Boolean existsByTheme(Long themeId);

    Boolean existsByTime(Long timeId);
}
