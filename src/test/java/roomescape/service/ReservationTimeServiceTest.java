package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {
    @InjectMocks
    private ReservationTimeService reservationTimeService;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("시간 추가 테스트")
    void test1() {
        // given & when
        LocalTime time = LocalTime.parse("12:00");
        when(reservationTimeRepository.addTime(any(ReservationTime.class))).thenReturn(
                new ReservationTime(1L, time));

        // then
        assertThat(reservationTimeService.addTime(time).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("전체 시간 조회 테스트")
    void test2() {
        // given & when
        LocalTime time = LocalTime.parse("12:00");
        LocalTime time2 = LocalTime.parse("13:00");
        reservationTimeService.addTime(time);
        reservationTimeService.addTime(time2);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        ReservationTime reservationTime2 = new ReservationTime(2L, time2);

        when(reservationTimeRepository.getAllTime()).thenReturn(List.of(reservationTime, reservationTime2));

        // then
        assertThat(reservationTimeService.getAllTime()).containsExactly(reservationTime, reservationTime2);
    }

    @Test
    @DisplayName("ID 시간 조회 테스트")
    void test3() {
        LocalTime time = LocalTime.parse("12:00");
        Long timeId = 1L;
        ReservationTime reservationTime = new ReservationTime(timeId, time);

        when(reservationTimeRepository.findById(timeId)).thenReturn(reservationTime);

        assertThat(reservationTimeService.getReservationTimeById(timeId).getId()).isEqualTo(timeId);
    }
}
