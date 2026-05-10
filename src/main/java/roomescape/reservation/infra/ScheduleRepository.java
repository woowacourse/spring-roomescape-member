package roomescape.reservation.infra;

import roomescape.reservation.domain.Schedule;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleRepository {
    Optional<Long> findScheduleIdByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId);

    Optional<Schedule> findById(long id);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);
}
