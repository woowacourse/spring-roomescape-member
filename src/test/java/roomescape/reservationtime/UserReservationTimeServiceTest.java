package roomescape.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class UserReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private UserReservationTimeService reservationTimeService;

    @Test
    void 예약_시간_목록을_조회할_수_있다() {
        List<ReservationTime> times = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new ReservationTime(2L, LocalTime.of(11, 0))
        );

        when(reservationTimeRepository.findAll()).thenReturn(times);

        List<ReservationTime> result = reservationTimeService.findReservationTimes();

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(ReservationTime::startAt)
                .containsExactly(LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    @Test
    void 스케줄_목록을_조회할_수_있다() {
        LocalDate date = LocalDate.of(2026, 5, 8);
        Long themeId = 1L;

        List<AvailableTimeDto> mockSchedules = List.of(
                new AvailableTimeDto(1L, LocalTime.of(10, 0), true),   // 예약 가능
                new AvailableTimeDto(2L, LocalTime.of(11, 0), false)   // 예약 불가 (이미 예약됨)
        );

        when(reservationTimeRepository.findAvailableTimes(date, themeId))
                .thenReturn(mockSchedules);

        ScheduleResponse result = reservationTimeService.getSchedules(date, themeId);

        assertThat(result.themeId()).isEqualTo(themeId);
        assertThat(result.date()).isEqualTo(date);
        assertThat(result.schedules()).hasSize(2);

        assertThat(result.schedules().get(0).isAvailable()).isTrue();
        assertThat(result.schedules().get(1).isAvailable()).isFalse();
    }

    @Test
    void 스케줄_목록이_비어있을_때() {
        LocalDate date = LocalDate.of(2026, 5, 8);
        Long themeId = 1L;

        when(reservationTimeRepository.findAvailableTimes(date, themeId))
                .thenReturn(Collections.emptyList());

        ScheduleResponse result = reservationTimeService.getSchedules(date, themeId);

        assertThat(result.themeId()).isEqualTo(themeId);
        assertThat(result.date()).isEqualTo(date);
        assertThat(result.schedules()).isEmpty();
    }
}
