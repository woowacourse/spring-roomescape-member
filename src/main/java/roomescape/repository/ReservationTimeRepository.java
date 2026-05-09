package roomescape.repository;

import roomescape.domain.ReservationTime;
import roomescape.service.dto.ReservationTimeAvailability;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    boolean existsByStartAt(LocalTime startAt);

    List<ReservationTimeAvailability> findAvailableTimes(LocalDate date, Long themeId);
}
