package roomescape.reservation.infra;

import roomescape.reservation.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    ReservationTime save(LocalTime startAt);

    List<ReservationTime> findAll();

    void deleteById(Long id);

    Optional<ReservationTime> findById(long id);

    List<ReservationTime> findTimesByDateAndThemeId(LocalDate date, long themeId);

    boolean existScheduleById(long timeId);
}
