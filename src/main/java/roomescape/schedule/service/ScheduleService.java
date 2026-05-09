package roomescape.schedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.schedule.dto.AdminScheduleRequest;
import roomescape.schedule.dto.ScheduleRequest;
import roomescape.schedule.dto.ScheduleResponse;
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

    private final ScheduleRepository scheduleRepository;
    private final ThemeService themeService;

    public ScheduleService(ScheduleRepository scheduleRepository, ThemeService themeService) {
        this.scheduleRepository = scheduleRepository;
        this.themeService = themeService;
    }

    public SchedulesResponse findAvailableSchedules(ScheduleRequest request) {
        List<Schedule> schedules = scheduleRepository.findAllAvailableByThemeAndDate(request.themeId(), request.date());
        return SchedulesResponse.from(schedules);
    }

    public List<ScheduleResponse> findAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스케줄입니다."));
    }

    @Transactional
    public ScheduleResponse createByAdmin(AdminScheduleRequest request) {
        Theme theme = themeService.findById(request.themeId());

        LocalDateTime newStartAt = LocalDateTime.of(request.date(), request.time());
        LocalTime requiredTime = theme.getRequiredTime();
        LocalDateTime newEndAt = newStartAt.plusHours(requiredTime.getHour())
                .plusMinutes(requiredTime.getMinute());

        List<Schedule> existingSchedules = scheduleRepository.findAllByThemeIdAndDate(request.themeId(), request.date());

        for (Schedule existingSchedule : existingSchedules) {
            if (existingSchedule.getStartAt().isBefore(newEndAt) && existingSchedule.getEndAt().isAfter(newStartAt)) {
                throw new IllegalArgumentException("선택하신 시간은 다른 예약 시간과 겹쳐서 추가할 수 없습니다.");
            }
        }

        Schedule newSchedule = new Schedule(newStartAt, theme);
        Long newScheduleId = scheduleRepository.create(newSchedule);
        Schedule savedSchedule = new Schedule(newScheduleId, newStartAt, theme);

        return ScheduleResponse.from(savedSchedule);
    }
}
