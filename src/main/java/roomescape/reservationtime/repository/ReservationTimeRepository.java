package roomescape.reservationtime.repository;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    List<ReservationTime> findAvailableTimes(LocalDate date, long themeId);

    Optional<ReservationTime> findById(long timeId);

    void deleteById(long timeId);

    ReservationTime save(ReservationTime reservationTime);

    boolean existsByStartAt(LocalTime startAt);

}
