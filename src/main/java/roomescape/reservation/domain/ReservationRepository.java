package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Reservation update(Reservation reservation);
    List<Reservation> findAll();
    List<Reservation> findByName(String name);
    List<Reservation> findByThemeAndDate(Long themeId, LocalDate date);
    Optional<Reservation> findById(Long id);
    boolean existsByReservationTime(Long timeId);
    boolean existsByDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId);
    void deleteById(Long id);
}
