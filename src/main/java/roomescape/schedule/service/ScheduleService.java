package roomescape.schedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.schedule.dto.ScheduleRequest;
import roomescape.schedule.dto.SchedulesResponse;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.service.ThemeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ThemeService themeService;

    public ScheduleService(ScheduleRepository scheduleRepository, ThemeService themeService) {
        this.scheduleRepository = scheduleRepository;
        this.themeService = themeService;
    }

    public SchedulesResponse findAll(ScheduleRequest request) {
        List<Schedule> schedules = scheduleRepository.findAll(request.themeId(), request.date());
        return SchedulesResponse.from(schedules);
    }

    @Transactional
    public Schedule CreateSchedule(LocalDate date, LocalTime time, Long themeId) {
        Theme theme = themeService.findById(themeId);

        LocalDateTime newStartAt = LocalDateTime.of(date, time);
        LocalTime requiredTime = theme.getRequiredTime();
        LocalDateTime newEndAt = newStartAt.plusHours(requiredTime.getHour())
                .plusMinutes(requiredTime.getMinute());

        List<Schedule> existingSchedules = scheduleRepository.findAll(themeId, date);

        for (Schedule existingSchedule : existingSchedules) {
            if (existingSchedule.getStartAt().isBefore(newEndAt) && existingSchedule.getEndAt().isAfter(newStartAt)) {
                throw new IllegalArgumentException("선택하신 시간은 다른 예약과 겹쳐서 예약할 수 없습니다.");
            }
        }

        Schedule newSchedule = new Schedule(newStartAt, theme);
        Long newScheduleId = scheduleRepository.create(newSchedule);

        return new Schedule(newScheduleId, newStartAt, theme);
    }
}
