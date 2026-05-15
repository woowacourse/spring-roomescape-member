package roomescape.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.ErrorCode;
import roomescape.exception.reservationtime.ReservationTimeNotFoundException;
import roomescape.exception.schedule.PastScheduleException;
import roomescape.exception.schedule.ScheduleAlreadyExistsException;
import roomescape.exception.schedule.ScheduleDeleteFailedException;
import roomescape.exception.schedule.ScheduleNotFoundException;
import roomescape.exception.schedule.ScheduleThemeInUseException;
import roomescape.exception.schedule.ScheduleTimeInUseException;
import roomescape.exception.theme.ThemeNotFoundException;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.schedule.dto.request.ScheduleSaveRequest;
import roomescape.schedule.dto.response.ScheduleFindResponse;
import roomescape.schedule.dto.response.ScheduleSaveResponse;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.repository.ThemeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public long findScheduleIdByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        return getScheduleIdOrThrow(date, timeId, themeId);
    }

    public List<ScheduleFindResponse> findAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return ScheduleFindResponse.from(schedules);
    }

    public ScheduleFindResponse findById(long id) {
        Schedule schedule = getScheduleOrElseThrow(id);
        return ScheduleFindResponse.from(schedule);
    }

    public ScheduleSaveResponse save(ScheduleSaveRequest body) {
        validateAlreadyExistsNot(body.date(), body.themeId(), body.timeId());
        return ScheduleSaveResponse.from(scheduleRepository.save(body.toDomain()));
    }

    private void validateAlreadyExistsNot(LocalDate date, long themeId, long timeId) {
        if (scheduleRepository.existsAlreadySchedule(date, themeId, timeId)){
            throw new ScheduleAlreadyExistsException(ErrorCode.SCHEDULE_ALREADY_EXIST);
        }
    }

    public void deleteById(long scheduleId) {
        if (scheduleRepository.deleteById(scheduleId) <= 1) {
            return;
        }
        throw new ScheduleDeleteFailedException(ErrorCode.RESERVATION_DELETE_FAILED);
    }

    public void validateTimeDeletable(long timeId) {
        if (scheduleRepository.existsByTimeId(timeId)) {
            throw new ScheduleTimeInUseException(ErrorCode.SCHEDULE_TIME_IN_USE, timeId);
        }
    }

    public void validateThemeDeletable(long themeId) {
        if (scheduleRepository.existsByThemeId(themeId)) {
            throw new ScheduleThemeInUseException(ErrorCode.SCHEDULE_THEME_IN_USE, themeId);
        }
    }

    public void validateSchedule(LocalDate date, Long timeId, Long themeId) {
        validateNotPastDate(date);
        ReservationTime reservationTime = getReservationTimeOrThrow(timeId);
        validateNotPastTime(date, reservationTime.getStartAt());
        getThemeOrThrow(themeId);
    }

    public void validateNotPastDate(LocalDate date) {
        if (date.isBefore(LocalDate.now(clock))) {
            throw new PastScheduleException(ErrorCode.PAST_SCHEDULE);
        }
    }

    public void validateNotPastTime(LocalDate date, LocalTime time) {
        LocalDate currentDate = LocalDate.now(clock);
        LocalTime currentTime = LocalTime.now(clock);

        if (date.isEqual(currentDate) && time.isBefore(currentTime)) {
            throw new PastScheduleException(ErrorCode.PAST_SCHEDULE);
        }
    }

    private void getThemeOrThrow(Long themeId) {
        themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException(ErrorCode.THEME_NOT_FOUND, themeId));
    }

    private ReservationTime getReservationTimeOrThrow(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeNotFoundException(ErrorCode.RESERVATIONTIME_NOT_FOUND, timeId));
    }

    private long getScheduleIdOrThrow(LocalDate date, long timeId, long themeId) {
        return scheduleRepository.findScheduleIdByDateAndTimeIdAndThemeId(date, timeId, themeId)
                .orElseThrow(() -> new ScheduleNotFoundException(ErrorCode.SCHEDULE_NOT_FOUND_WITH_CONDITION, date, timeId, themeId));
    }

    private Schedule getScheduleOrElseThrow(long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(ErrorCode.SCHEDULE_NOT_FOUND, scheduleId));
    }
}
