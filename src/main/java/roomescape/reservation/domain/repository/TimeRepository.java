package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.dto.TimeDataWithBookingInfo;

public interface TimeRepository {
    Long save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    void deleteById(Long id);

    List<TimeDataWithBookingInfo> getTimesWithBookingInfo(LocalDate date, Long themeId);
}
