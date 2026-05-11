package roomescape.repository;

import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    ReservationTime addTime(ReservationTime reservationTime);

    List<ReservationTime> findAllReservationTimes();

    void deleteTime(Long id);

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> findAvailableTimes(Long themeId, LocalDate date);
}
