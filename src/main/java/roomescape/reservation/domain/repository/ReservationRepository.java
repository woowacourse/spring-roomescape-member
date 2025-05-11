package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Long save(Reservation reservation);

    void deleteById(Long id);

    boolean existsDuplicatedReservation(LocalDate date, Long timeId, Long themeId);
}
