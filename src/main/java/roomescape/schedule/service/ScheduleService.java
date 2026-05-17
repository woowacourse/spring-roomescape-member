package roomescape.schedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.schedule.dto.AdminScheduleRequest;
import roomescape.schedule.dto.ScheduleRequest;
import roomescape.schedule.dto.SchedulesResponse;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.exception.ErrorCode;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    public static final LocalTime OPENING_TIME = LocalTime.of(10, 0);
    public static final LocalTime CLOSE_TIME = LocalTime.of(20, 0);

    private final ScheduleRepository scheduleRepository;
    private final ThemeService themeService;
    private final Clock clock;

    public ScheduleService(ScheduleRepository scheduleRepository, ThemeService themeService, Clock clock) {
        this.scheduleRepository = scheduleRepository;
        this.themeService = themeService;
        this.clock = clock;
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
                .orElseThrow(() -> new NotFoundException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    @Transactional
    public Long create(AdminScheduleRequest request) {
        LocalDateTime newStartAt = LocalDateTime.of(request.date(), request.time());
        validateOpeningTime(newStartAt);

        Theme theme = themeService.findById(request.themeId());
        LocalDateTime newEndAt = calculateEndAt(newStartAt, theme.getRequiredTime());

        validateCloseTime(newEndAt);
        validateDuplicateSchedule(request.themeId(), request.date(), newStartAt, newEndAt);

        Schedule newSchedule = new Schedule(newStartAt, theme);
        return scheduleRepository.create(newSchedule);
    }

    @Transactional
    public void delete(Long scheduleId) {
        scheduleRepository.delete(scheduleId);
    }

    private void validateOpeningTime(LocalDateTime startAt) {
        if (startAt.isBefore(LocalDateTime.now(clock))) {
            throw new BadRequestException(ErrorCode.PAST_SCHEDULE_CREATION);
        }
        if (startAt.toLocalTime().isBefore(OPENING_TIME)) {
            throw new BadRequestException(ErrorCode.INVALID_SCHEDULE_TIME);
        }
    }

    private LocalDateTime calculateEndAt(LocalDateTime startAt, LocalTime requiredTime) {
        return startAt.plusHours(requiredTime.getHour())
                .plusMinutes(requiredTime.getMinute());
    }

    private void validateCloseTime(LocalDateTime endAt) {
        if (endAt.toLocalTime().isAfter(CLOSE_TIME)) {
            throw new BadRequestException(ErrorCode.INVALID_SCHEDULE_TIME);
        }
    }

    private void validateDuplicateSchedule(Long themeId, LocalDate date, LocalDateTime startAt, LocalDateTime endAt) {
        List<Schedule> existingSchedules = scheduleRepository.findDailySchedules(themeId, date);
        for (Schedule existingSchedule : existingSchedules) {
            if (existingSchedule.getStartAt().isBefore(endAt) && existingSchedule.getEndAt().isAfter(startAt)) {
                throw new ConflictException(ErrorCode.DUPLICATE_SCHEDULE_TIME);
            }
        }
    }
}
