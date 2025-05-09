package roomescape.reservationtime.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeDao {

    long insert(ReservationTime reservationTime);

    boolean existsByStartAt(LocalTime startAt);

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(long id);

    List<ReservationTime> findAllBookedTime(LocalDate date, long themeId);

    boolean deleteById(long id);
}
