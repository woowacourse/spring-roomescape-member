package roomescape.reservation.repository;

import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void cancelById(Long id);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll(LocalDateTime now);

    List<Reservation> findByFilter(String name, LocalDate from, LocalDate to, Long themeId, LocalDateTime now);

    void update(Reservation reservation);

    boolean existsByDateAndTimeIdAndThemeIdExcluding(LocalDate date, Long timeId, Long themeId, Long excludeId);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existsByTimeId(Long timeId);

    boolean existsByThemeId(Long themeId);

    int completeAllPastReservations(LocalDateTime now);
}
