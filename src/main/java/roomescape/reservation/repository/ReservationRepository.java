package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    List<ReservationDetail> findAll();

    Reservation save(Reservation reservation);

    void delete(Long id);

    Boolean existsByDateAndThemeAndTime(LocalDate date, Long themeId, Long timeId);
}
