package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDAO {

    long insert(ReservationTime reservationTime);

    boolean existsByStartAt(LocalTime startAt);

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(long id);

    boolean deleteById(long id);

    List<ReservationTime> findAllBookedTime(LocalDate date, long themeId);
}
