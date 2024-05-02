package roomescape.repository.reservation;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    void deleteById(Long id);

    boolean existsByTimeId(Long timeId);

    boolean existsByThemeId(Long themeId);

    List<Reservation> findByDateBetween(LocalDate start, LocalDate end);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);
}
