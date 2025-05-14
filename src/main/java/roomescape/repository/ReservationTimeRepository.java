package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> readAll();

    Optional<ReservationTime> findById(Long id);

    void delete(Long id);

    List<ReservationTime> findAvailableTimesBy(LocalDate date, Long themeId);
}
