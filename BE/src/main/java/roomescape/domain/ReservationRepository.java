package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);
    Optional<Reservation> findByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);
    List<Reservation> findAll();
    boolean existsByReservationTimeId(Long id);
    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);
    void deleteById(Long id);
}
