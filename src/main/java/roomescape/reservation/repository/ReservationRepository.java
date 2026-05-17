package roomescape.reservation.repository;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void update(Reservation reservation);

    void updateStatus(Long id, ReservationStatus status);

    Optional<Reservation> findById(Long id);

    List<Reservation> findByFilter(String name, LocalDate from, LocalDate to, Long themeId);

    boolean existsByDateAndTimeIdAndThemeIdAndStatus(LocalDate date, Long timeId, Long themeId, ReservationStatus status);

    boolean existsByDateAndTimeIdAndThemeIdAndStatusExcludingSelf(LocalDate date, Long timeId, Long themeId, Long excludeId, ReservationStatus status);

    boolean existsByTimeId(Long timeId);

    boolean existsByThemeId(Long themeId);
}
