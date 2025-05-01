package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAllTimes();

    ReservationTime findTimeById(Long id);

    List<ReservationTime> findAllTimesWithBooked(LocalDate date, Long themeId);

    ReservationTime addTime(ReservationTime reservationTime);

    boolean existTimeByStartAt(LocalTime startAt);

    void removeTimeById(Long id);
}
