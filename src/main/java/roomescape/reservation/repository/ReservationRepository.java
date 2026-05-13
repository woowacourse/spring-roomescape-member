package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Reservation> findByUserName(String name);

    List<Reservation> findByThemeAndDate(Long themeId, LocalDate date);

    void update(Reservation reservation);

    boolean existsByTimeId(Long timeId);

    boolean existsActiveByDateAndThemeAndTime(LocalDate date, Long themeId, Long timeId);
}
