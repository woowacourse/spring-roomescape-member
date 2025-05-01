package roomescape.reservationtime.dao;

import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.ReservationTime;

public interface ReservationTimeDao {
    List<ReservationTime> findAll();

    Long create(ReservationTime reservationTime);

    void delete(Long id);

    Optional<ReservationTime> findById(Long id);

//    List<ReservationTime> findTimesByThemeId(LocalDate date, Long themeId);
}
