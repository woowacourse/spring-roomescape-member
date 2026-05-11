package roomescape.schedule.repository;

import roomescape.schedule.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);

    Optional<Long> findScheduleIdByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId);

    Optional<Schedule> findById(long id);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    List<Schedule> findAll();

    int deleteById(long id);

    Optional<Long> findThemeIdById(long id);

    Optional<Long> findTimeIdById(long id);
}
