package roomescape.reservation.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.infra.ReservationTimeRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ReservationTimeServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약이 존재하면 시간 삭제에 실패한다.")
    void delete_실패_테스트_1() {
        // given
        long timeId = 1L;
        when(reservationRepository.existReservationByTimeId(timeId)).thenReturn(true);
        when(reservationTimeRepository.existScheduleById(timeId)).thenReturn(false);

        // when, then
        assertThatThrownBy(() -> reservationTimeService.delete(timeId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 예약 시간은 예약 또는 스케줄에서 사용 중이므로 삭제할 수 없습니다.");

        verify(reservationTimeRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("스케줄이 존재하면 시간 삭제에 실패한다.")
    void delete_실패_테스트_2() {
        // given
        long timeId = 1L;
        when(reservationRepository.existReservationByTimeId(timeId)).thenReturn(false);
        when(reservationTimeRepository.existScheduleById(timeId)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> reservationTimeService.delete(timeId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 예약 시간은 예약 또는 스케줄에서 사용 중이므로 삭제할 수 없습니다.");

        verify(reservationTimeRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("예약과 스케줄에 시간에 대한 참조가 존재하지 않으면 시간 삭제에 성공한다.")
    void delete_성공_테스트() {
        // given
        long timeId = 1L;
        when(reservationRepository.existReservationByTimeId(timeId)).thenReturn(false);
        when(reservationTimeRepository.existScheduleById(timeId)).thenReturn(false);

        // when
        reservationTimeService.delete(timeId);

        // then
        verify(reservationTimeRepository).deleteById(anyLong());
    }

}
