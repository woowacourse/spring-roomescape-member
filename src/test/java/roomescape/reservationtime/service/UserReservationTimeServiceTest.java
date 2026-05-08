package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.AvailableTime;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

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

        ReservationTime time1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime time2 = new ReservationTime(2L, LocalTime.of(11, 0));
        when(reservationTimeRepository.findAll()).thenReturn(List.of(time1, time2));

        when(reservationRepository.findReservedTimeIds(date, themeId))
                .thenReturn(List.of(2L));

        List<AvailableTime> result = reservationTimeService.getSchedules(date, themeId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).isAvailable()).isTrue();
        assertThat(result.get(1).isAvailable()).isFalse();
    }

    @Test
    void 전체_시간_목록이_없으면_스케줄_목록도_비어있다() {
        LocalDate date = LocalDate.of(2026, 5, 8);
        Long themeId = 1L;

        when(reservationTimeRepository.findAll()).thenReturn(Collections.emptyList());

        List<AvailableTime> result = reservationTimeService.getSchedules(date, themeId);
        assertThat(result).isEmpty();
    }
}
