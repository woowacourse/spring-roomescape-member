package roomescape.time.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.time.domain.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> findAll();

    boolean existsByStartAt(LocalTime startAt);

    List<AvailableTimeQueryResult> findAvailableTimes(Long themeId, LocalDate date);
}
