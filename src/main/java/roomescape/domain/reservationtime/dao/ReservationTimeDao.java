package roomescape.domain.reservationtime.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.reservationtime.model.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    long save(ReservationTime reservationTime);

    boolean delete(Long id);

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> findBookedTimes(String date, Long themeId);
}
