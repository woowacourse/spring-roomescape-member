package roomescape.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.schedule.dto.request.ScheduleSaveRequest;
import roomescape.schedule.dto.response.ScheduleFindResponse;
import roomescape.schedule.dto.response.ScheduleSaveResponse;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public void validateTimeDeletable(long timeId) {
        if (scheduleRepository.existsByTimeId(timeId)) {
            throw new IllegalStateException("timeId= " + timeId + " 인 시간을 사용하는 스케줄이 있어 삭제할 수 없습니다.");
        }
    }

    public void validateThemeDeletable(long themeId) {
        if (scheduleRepository.existsByThemeId(themeId)) {
            throw new IllegalStateException("themeId= " + themeId + " 인 테마를 사용하는 스케줄이 있어 삭제할 수 없습니다.");
        }
    }

    public Long findScheduleIdByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        return scheduleRepository.findScheduleIdByDateAndTimeIdAndThemeId(date, timeId, themeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 조건을 가진 일정이 없습니다. date: " + date + "timeId: " + timeId + "themeId: " + themeId
                ));
    }

    public List<ScheduleFindResponse> findAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return ScheduleFindResponse.from(schedules);
    }

    public ScheduleFindResponse findById(long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 스케줄이 존재하지 않습니다. ID=" + id));
        return ScheduleFindResponse.from(schedule);
    }

    public ScheduleSaveResponse save(ScheduleSaveRequest body) {
        Schedule newSchedule = scheduleRepository.save(body.toDomain());
        return ScheduleSaveResponse.from(newSchedule);
    }

    public void deleteById(long scheduleId) {
        if (scheduleRepository.deleteById(scheduleId) != 1) {
            throw new IllegalStateException("해당 ID를 가진 스케줄을 삭제하지 못하였습니다 ID=" + scheduleId);
        }
    }
}
