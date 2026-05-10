package roomescape.time.repository;

import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    boolean existsByStartAt(LocalTime startAt);

    List<ReservationTime> findAll();

    List<ReservationTime> findAvailableTimes(Long themeId, LocalDate date);
}
