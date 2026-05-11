package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    List<Reservation> findAll();
    List<Reservation> findByThemeAndDate(Long aLong, LocalDate date);
    boolean existsByReservationTime(Long timeId);
    boolean existsById(Long id);
    boolean existsByDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId);
    void deleteById(Long id);
}
