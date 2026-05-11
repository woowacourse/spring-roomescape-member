package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Integer delete(Long id);

    Boolean existsByDateAndThemeAndTime(LocalDate date, Long themeId, Long timeId);
}
