package roomescape.schedule.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.schedule.dto.AdminScheduleRequest;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.exception.ErrorCode;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ScheduleServiceTest {

    private ScheduleService scheduleService;
    private ScheduleRepository scheduleRepository;
    private ThemeService themeService;
    private Clock clock;

    private final Theme theme = new Theme(1L, "테마", "설명", "경로", LocalTime.of(2, 0));

    @BeforeEach
    void setUp() {
        scheduleRepository = Mockito.mock(ScheduleRepository.class);
        themeService = Mockito.mock(ThemeService.class);
        clock = Clock.fixed(Instant.parse("2024-05-16T10:00:00Z"), ZoneId.of("Asia/Seoul"));

        scheduleService = new ScheduleService(scheduleRepository, themeService, clock);
    }

    @Test
    void 존재하지_않는_스케줄_조회_예외() {
        // given
        when(scheduleRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> scheduleService.findById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.SCHEDULE_NOT_FOUND.getMessage());
    }

    @Test
    void 새로운_스케줄을_성공적으로_생성한다() {
        // given
        LocalDate futureDate = LocalDate.now(clock).plusDays(1);
        LocalTime time = LocalTime.of(15, 0);
        AdminScheduleRequest request = new AdminScheduleRequest(theme.getId(), futureDate, time);

        when(themeService.findById(theme.getId())).thenReturn(theme);

        when(scheduleRepository.findDailySchedules(request.themeId(), request.date()))
                .thenReturn(Collections.emptyList());

        when(scheduleRepository.create(any(Schedule.class))).thenReturn(1L);

        // when
        Long createdId = scheduleService.create(request);

        // then
        assertThat(createdId).isEqualTo(1L);
    }

    @Test
    void 과거_시간_스케줄_생성_예외() {
        // given
        LocalDate pastDate = LocalDate.now(clock).minusDays(1);
        LocalTime time = LocalTime.of(12, 0);
        AdminScheduleRequest request = new AdminScheduleRequest(theme.getId(), pastDate, time);

        // when & then
        assertThatThrownBy(() -> scheduleService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.PAST_SCHEDULE_CREATION.getMessage());
    }

    @Test
    void 오픈_시간_이전_스케줄_생성_예외() {
        // given
        LocalDate date = LocalDate.now(clock).plusDays(1);
        LocalTime time = LocalTime.of(9, 59);
        AdminScheduleRequest request = new AdminScheduleRequest(theme.getId(), date, time);

        // when & then
        assertThatThrownBy(() -> scheduleService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_SCHEDULE_TIME.getMessage());
    }

    @Test
    void 마감_시간_이후_종료_스케줄_생성_예외() {
        // given
        LocalDate date = LocalDate.now(clock).plusDays(1);
        LocalTime time = LocalTime.of(19, 0);
        AdminScheduleRequest request = new AdminScheduleRequest(theme.getId(), date, time);

        when(themeService.findById(theme.getId())).thenReturn(theme);

        // when & then
        assertThatThrownBy(() -> scheduleService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_SCHEDULE_TIME.getMessage());
    }

    @Test
    void 이미_있는_예약_시간과_겹치는_스케줄을_생성하면_예외가_발생한다() {
        // given
        LocalDate date = LocalDate.now(clock).plusDays(1);
        LocalTime overlappingTime = LocalTime.of(15, 0);
        AdminScheduleRequest request = new AdminScheduleRequest(theme.getId(), date, overlappingTime);

        Schedule existingSchedule = new Schedule(2L, LocalDateTime.of(date, LocalTime.of(14, 0)), theme);

        when(themeService.findById(theme.getId())).thenReturn(theme);

        when(scheduleRepository.findDailySchedules(request.themeId(), request.date()))
                .thenReturn(List.of(existingSchedule));

        // when & then
        assertThatThrownBy(() -> scheduleService.create(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage(ErrorCode.DUPLICATE_SCHEDULE_TIME.getMessage());
    }

    @Test
    void 스케줄을_성공적으로_삭제한다() {
        // given
        Long scheduleId = 1L;

        // when
        scheduleService.delete(scheduleId);

        // then
        Mockito.verify(scheduleRepository, Mockito.times(1)).delete(scheduleId);
    }

    @Test
    void 예약이_존재하는_스케줄_삭제_예외() {
        // given
        Mockito.doThrow(new DataIntegrityViolationException("FK Constraint Violation"))
                .when(scheduleRepository).delete(1L);

        // when & then
        assertThatThrownBy(() -> scheduleService.delete(1L))
                .isInstanceOf(ConflictException.class)
                .hasMessage(ErrorCode.SCHEDULE_IN_USE.getMessage());
    }
}
