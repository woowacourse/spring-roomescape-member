package roomescape.reservation.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.domain.Schedule;
import roomescape.reservation.infra.ScheduleRepository;
import roomescape.reservation.presentation.dto.request.ScheduleSaveRequest;
import roomescape.reservation.presentation.dto.response.ScheduleFindResponse;
import roomescape.reservation.presentation.dto.response.ScheduleSaveResponse;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    @DisplayName("스케줄 저장에 성공한다.")
    void save_성공_테스트() {
        ScheduleSaveRequest request = new ScheduleSaveRequest(LocalDate.of(2026, 5, 10), 1L, 2L);
        Schedule savedSchedule = new Schedule(10L, LocalDate.of(2026, 5, 10), 1L, 2L);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(savedSchedule);

        ScheduleSaveResponse response = scheduleService.save(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.date()).isEqualTo(LocalDate.of(2026, 5, 10));
        assertThat(response.time_id()).isEqualTo(1L);
        assertThat(response.theme_id()).isEqualTo(2L);
    }

    @Test
    @DisplayName("ID로 스케줄 단건 조회에 성공한다.")
    void findById_성공_테스트() {
        Schedule schedule = new Schedule(1L, LocalDate.of(2026, 5, 5), 1L, 1L);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        ScheduleFindResponse response = scheduleService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.date()).isEqualTo(LocalDate.of(2026, 5, 5));
        assertThat(response.time_id()).isEqualTo(1L);
        assertThat(response.theme_id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("스케줄 삭제에 성공한다.")
    void delete_성공_테스트() {
        when(scheduleRepository.deleteById(1L)).thenReturn(1);

        scheduleService.deleteById(1L);

        verify(scheduleRepository).deleteById(1L);
    }

    @Test
    @DisplayName("삭제할 스케줄이 없으면 삭제에 실패한다.")
    void delete_실패_테스트() {
        when(scheduleRepository.deleteById(1L)).thenReturn(0);

        assertThatThrownBy(() -> scheduleService.deleteById(1L))
                .isInstanceOf(IllegalStateException.class);
    }
}
