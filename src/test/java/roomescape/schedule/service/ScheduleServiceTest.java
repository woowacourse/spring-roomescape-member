package roomescape.schedule.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.dto.AdminScheduleRequest;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.service.ThemeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ScheduleServiceTest {

    private ScheduleService scheduleService;
    private ReservationRepository reservationRepository;
    private ScheduleRepository scheduleRepository;
    private ThemeService themeService;

    private final Theme theme = new Theme(1L, "테마", "설명", "경로", LocalTime.of(2, 0));

    @BeforeEach
    void setUp() {
        scheduleRepository = Mockito.mock(ScheduleRepository.class);
        themeService = Mockito.mock(ThemeService.class);
        reservationRepository = Mockito.mock(ReservationRepository.class);

        scheduleService = new ScheduleService(scheduleRepository, reservationRepository, themeService);
    }

    @Test
    void 새로운_스케줄을_성공적으로_생성한다() {
        // given
        LocalDate futureDate = LocalDate.now().plusDays(1);
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
    void 이미_있는_예약_시간과_겹치는_스케줄을_생성하면_예외가_발생한다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime overlappingTime = LocalTime.of(15, 0);
        AdminScheduleRequest request = new AdminScheduleRequest(theme.getId(), date, overlappingTime);

        Schedule existingSchedule = new Schedule(2L, LocalDateTime.of(date, LocalTime.of(14, 0)), theme);

        when(themeService.findById(theme.getId())).thenReturn(theme);

        when(scheduleRepository.findDailySchedules(request.themeId(), request.date()))
                .thenReturn(List.of(existingSchedule));

        // when & then
        assertThatThrownBy(() -> scheduleService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택하신 시간은 다른 예약 시간과 겹쳐서 추가할 수 없습니다.");
    }
}
