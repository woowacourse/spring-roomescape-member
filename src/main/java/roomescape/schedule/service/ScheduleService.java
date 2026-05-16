package roomescape.schedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.dto.AdminScheduleRequest;
import roomescape.schedule.dto.ScheduleRequest;
import roomescape.schedule.dto.SchedulesResponse;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.exception.ErrorCode;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    public static final LocalTime OPENING_TIME = LocalTime.of(10, 0);
    public static final LocalTime CLOSE_TIME = LocalTime.of(20, 0);

    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeService themeService;

    public ScheduleService(ScheduleRepository scheduleRepository, ReservationRepository reservationRepository, ThemeService themeService) {
        this.scheduleRepository = scheduleRepository;
        this.reservationRepository = reservationRepository;
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
                .orElseThrow(() -> new NotFoundException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    // TODO: 메서드가 너무 길다. (2026. 5. 12.)
    @Transactional
    public Long create(AdminScheduleRequest request) {
        Theme theme = themeService.findById(request.themeId());
        LocalDateTime newStartAt = LocalDateTime.of(request.date(), request.time());

        if (newStartAt.isBefore(LocalDateTime.now())) {
            throw new BadRequestException(ErrorCode.PAST_SCHEDULE_CREATION);
        }

        if (newStartAt.toLocalTime().isBefore(OPENING_TIME)) {
            throw new BadRequestException(ErrorCode.INVALID_SCHEDULE_TIME);
        }

        LocalTime requiredTime = theme.getRequiredTime();
        LocalDateTime newEndAt = newStartAt.plusHours(requiredTime.getHour())
                .plusMinutes(requiredTime.getMinute());
        if (newEndAt.toLocalTime().isAfter(CLOSE_TIME)) {
            throw new BadRequestException(ErrorCode.INVALID_SCHEDULE_TIME);
        }

        List<Schedule> existingSchedules = scheduleRepository.findDailySchedules(request.themeId(), request.date());
        for (Schedule existingSchedule : existingSchedules) {
            if (existingSchedule.getStartAt().isBefore(newEndAt) && existingSchedule.getEndAt().isAfter(newStartAt)) {
                throw new ConflictException(ErrorCode.DUPLICATE_SCHEDULE_TIME);
            }
        }

        Schedule newSchedule = new Schedule(newStartAt, theme);
        return scheduleRepository.create(newSchedule);
    }

    @Transactional
    public void delete(Long scheduleId) {
        if (reservationRepository.existsByScheduleId(scheduleId)) {
            throw new BadRequestException(ErrorCode.SCHEDULE_IN_USE);
        }
        scheduleRepository.delete(scheduleId);
    }
}
