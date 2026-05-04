package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    List<Reservation> findAll();
    List<Reservation> findByThemeAndDate(Long themeId, LocalDate date);
    boolean existsByReservationTime(Long timeId);
    boolean existsById(Long id);
    void deleteById(Long id);
}
