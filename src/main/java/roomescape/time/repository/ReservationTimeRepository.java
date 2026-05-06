package roomescape.time.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.time.domain.ReservationTime;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    ReservationTime save(ReservationTime reservationTime);

    void delete(Long id);

    boolean existsByStartAt(LocalTime startAt);

    List<ReservationTime> findAvailableByDateAndThemeId(LocalDate date, Long themeId);
}
