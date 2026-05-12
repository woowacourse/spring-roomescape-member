package roomescape.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    boolean existsByReservationTimeId(Long id);

    boolean existsByThemeId(Long themeId);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    List<Reservation> findByName(String name);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    void update(Reservation reservation);

    void deleteById(Long id);
}
