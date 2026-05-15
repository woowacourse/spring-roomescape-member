package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    List<ReservationDetail> findAll();

    List<Reservation> findByName(String name);

    Optional<Reservation> findById(Long id);

    Reservation save(Reservation reservation);

    Integer delete(Long id);

    Boolean existsByDateAndThemeAndTime(LocalDate date, Long themeId, Long timeId);
}
