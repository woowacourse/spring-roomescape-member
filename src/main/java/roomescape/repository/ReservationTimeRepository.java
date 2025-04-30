package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime saveReservationTime(ReservationTime reservationTime);
    List<ReservationTime> readReservationTimes();
    Optional<ReservationTime> readReservationTime(Long id);
    void deleteReservationTime(Long id);
    List<ReservationTime> findAvailableTimesBy(LocalDate date, Long themeId);
}
