package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.model.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Reservation> findAllByTimeId(Long timeId);

    List<Reservation> findAllByThemeId(Long themeId);

    List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId);

    boolean existsByDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId);

    boolean existsById(Long id);

    boolean existsByTimeId(Long id);

    void deleteById(Long id);
}
