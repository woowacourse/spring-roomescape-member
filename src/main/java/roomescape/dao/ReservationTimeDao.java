package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {
    ReservationTime create(ReservationTime reservationTimeWithoutId);

    ReservationTime read(Long id);

    List<ReservationTime> readAll();

    void delete(Long id);

    List<Long> reservedTimeIdByDateAndTheme(LocalDate date, Long themeId);
}
