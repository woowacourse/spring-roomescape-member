package roomescape.repository.time;

import roomescape.domain.ReservationTime;
import roomescape.domain.vo.ReservationDate;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime createReservationTime(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> findAvailableTimes(ReservationDate date, Long themeId);
}
