package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    private static final LocalDate FIXED_TODAY = LocalDate.of(2026, 5, 8);
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(
                FIXED_TODAY.atStartOfDay(ZONE).toInstant(),
                ZONE);
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, fixedClock);
    }

    @Test
    void 날짜가_있으면_예약_가능_시간을_조회한다() {
        LocalDate date = LocalDate.of(2026, 5, 3);
        List<ReservationTime> times = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new ReservationTime(2L, LocalTime.of(11, 0))
        );

        when(reservationTimeRepository.findAvailableTimes(1L, date)).thenReturn(times);
        List<ReservationTime> result = reservationTimeService.getAvailableTimes(1L, date);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(times);
    }

    @Test
    void 날짜가_없으면_오늘_예약_가능_시간을_조회한다() {
        List<ReservationTime> times = List.of(new ReservationTime(1L, LocalTime.of(10, 0)));

        when(reservationTimeRepository.findAvailableTimes(1L, FIXED_TODAY)).thenReturn(times);
        List<ReservationTime> result = reservationTimeService.getAvailableTimes(1L, null);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result).isEqualTo(times);
    }
}
