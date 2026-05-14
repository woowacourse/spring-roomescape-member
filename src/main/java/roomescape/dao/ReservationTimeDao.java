package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {
    ReservationTime create(ReservationTime reservationTime);

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> findAll();

    void delete(Long id);

    List<Long> findBookedTimeIdsByDateAndTheme(LocalDate date, Long themeId);
}
