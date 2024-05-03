package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    boolean existByStartAt(LocalTime startAt);

    ReservationTime insert(ReservationTime reservationTime);

    void deleteById(Long id);

    List<ReservationTime> findReservedTimes(LocalDate date, Long themeId);
}
