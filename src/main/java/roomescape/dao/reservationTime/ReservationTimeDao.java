package roomescape.dao.reservationTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    ReservationTime create(ReservationTime reservationTime);

    boolean deleteIfNoReservation(final long id);

    Optional<ReservationTime> findById(long id);

    Optional<ReservationTime> findByIdAndDateAndTheme(long id, long themeId, LocalDate date);
}
