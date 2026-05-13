package roomescape.reservationtime.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    List<ReservationTime> findAllByThemeId(long themeId);

    Optional<ReservationTime> findById(long timeId);

    List<ReservationTime> findAvailableTimes(LocalDate date, Long themeId);

    void deleteById(long timeId);

    ReservationTime save(ReservationTime reservationTime);

    boolean existsByStartAtAndThemeId(final LocalTime startAt, final long themeId);

}
