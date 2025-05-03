package roomescape.dao.reservationTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    ReservationTime create(ReservationTime reservationTime);

    boolean deleteIfNoReservation(Long id);

    Optional<ReservationTime> findById(Long id);

    Optional<ReservationTime> findByIdAndDateAndTheme(Long id, Long themeId, LocalDate date);
}
