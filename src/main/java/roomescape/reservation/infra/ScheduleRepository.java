package roomescape.reservation.infra;

import roomescape.reservation.domain.Schedule;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleRepository {
    Optional<Schedule> findByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);
}
