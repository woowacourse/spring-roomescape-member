package roomescape.schedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.schedule.dto.AdminScheduleRequest;
import roomescape.schedule.dto.ScheduleRequest;
import roomescape.schedule.dto.SchedulesResponse;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.service.ThemeService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    public static final LocalTime OPENING_TIME = LocalTime.of(20, 0);
    public static final LocalTime CLOSE_TIME = LocalTime.of(20, 0);

    private final ScheduleRepository scheduleRepository;
    private final ThemeService themeService;

    public ScheduleService(ScheduleRepository scheduleRepository, ThemeService themeService) {
        this.scheduleRepository = scheduleRepository;
        this.themeService = themeService;
    }

    public SchedulesResponse findAvailableSchedules(ScheduleRequest request) {
        List<Schedule> schedules = scheduleRepository.findReservableSchedules(request.themeId(), request.date());
        return SchedulesResponse.from(schedules);
    }

    public SchedulesResponse findAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return SchedulesResponse.from(schedules);
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스케줄입니다."));
    }

    // TODO: 메서드가 너무 길다. (2026. 5. 12.)
    @Transactional
    public Long create(AdminScheduleRequest request) {
        Theme theme = themeService.findById(request.themeId());
        LocalDateTime newStartAt = LocalDateTime.of(request.date(), request.time());

        if (newStartAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 날짜/시간에는 스케줄을 생성할 수 없습니다.");
        }

        if (newStartAt.toLocalTime().isBefore(OPENING_TIME)) {
            throw new IllegalArgumentException("오전 10시 이전에는 예약이 불가능합니다.");
        }

        LocalTime requiredTime = theme.getRequiredTime();
        LocalDateTime newEndAt = newStartAt.plusHours(requiredTime.getHour())
                .plusMinutes(requiredTime.getMinute());
        if (newEndAt.toLocalTime().isAfter(CLOSE_TIME)) {
            throw new IllegalArgumentException("오후 8시 이후에는 예약이 불가능합니다.");
        }

        List<Schedule> existingSchedules = scheduleRepository.findDailySchedules(request.themeId(), request.date());
        for (Schedule existingSchedule : existingSchedules) {
            if (existingSchedule.getStartAt().isBefore(newEndAt) && existingSchedule.getEndAt().isAfter(newStartAt)) {
                throw new IllegalArgumentException("선택하신 시간은 다른 예약 시간과 겹쳐서 추가할 수 없습니다.");
            }
        }

        Schedule newSchedule = new Schedule(newStartAt, theme);
        return scheduleRepository.create(newSchedule);
    }
}
