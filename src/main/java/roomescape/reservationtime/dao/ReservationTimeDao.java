package roomescape.reservationtime.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    ReservationTime create(ReservationTime reservationTime);

    void delete(ReservationTime reservationTime);

    Optional<ReservationTime> findById(Long id);

    Optional<ReservationTime> findByIdAndDateAndTheme(Long id, Long themeId, LocalDate date);
}
