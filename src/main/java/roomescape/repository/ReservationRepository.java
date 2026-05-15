package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAllReservations();

    Reservation addReservation(Reservation reservation);

    void updateDateTime(Long id, LocalDate date, Long timeId);

    void deleteById(Long id);

    List<Reservation> findReservationsByName(String name);

    Optional<Reservation> findById(Long id);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existsConflictingReservation(LocalDate date, Long timeId, Long themeId, Long id);

    boolean existsById(Long id);

    boolean existsByThemeId(Long themeId);
}
