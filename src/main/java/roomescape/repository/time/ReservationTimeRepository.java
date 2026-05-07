package roomescape.repository.time;

import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime createReservationTime(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(long id);

    Optional<ReservationTime> findById(long id);

    List<ReservationTime> findByDateAndThemeId(LocalDate date, long themeId);
}
