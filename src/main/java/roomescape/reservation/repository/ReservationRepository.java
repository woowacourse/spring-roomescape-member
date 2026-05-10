package roomescape.reservation.repository;

import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    void deleteAll();

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    List<Reservation> findByFilter(LocalDate date, Long themeId);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existsByTimeId(Long timeId);

    boolean existsByThemeId(Long themeId);
}
