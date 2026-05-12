package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {
    ReservationTime create(ReservationTime reservationTime);

    Optional<ReservationTime> read(Long id);

    List<ReservationTime> readAll();

    void delete(Long id);

    List<Long> bookedTimeIdByDateAndTheme(LocalDate date, Long themeId);
}
