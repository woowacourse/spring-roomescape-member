package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

public interface ReservationRepository {
    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Reservation> findByName(String name);

    Reservation save(Reservation reservation);

    List<Long> findTimeIdsByThemeIdAndDate(Long themeId, LocalDate date);

    void update(Long id, LocalDate date, Long timeId, Long themeId);

    boolean deleteById(Long id);

    boolean isDuplicated(Long themeId, ReservationTime time, LocalDate date);

    boolean existsByTimeId(Long timeId);
}
