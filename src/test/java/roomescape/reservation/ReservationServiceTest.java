package roomescape.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.ScheduleService;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 삭제 시 삭제 건수가 0이면 예외가 발생하지 않는다.")
    void deleteById_삭제건수0_성공() {
        when(reservationRepository.deleteById(1L)).thenReturn(0);

        assertThatCode(() -> reservationService.deleteById(1L))
                .doesNotThrowAnyException();
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("예약 삭제 시 삭제 건수가 1이면 예외가 발생하지 않는다.")
    void deleteById_삭제건수1_성공() {
        when(reservationRepository.deleteById(1L)).thenReturn(1);

        assertThatCode(() -> reservationService.deleteById(1L))
                .doesNotThrowAnyException();
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("예약 삭제 시 삭제 건수가 2 이상이면 예외가 발생한다.")
    void deleteById_삭제건수2이상_실패() {
        when(reservationRepository.deleteById(1L)).thenReturn(2);

        assertThatThrownBy(() -> reservationService.deleteById(1L))
                .isInstanceOf(IllegalStateException.class);

        verify(reservationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("이름 기준 예약 삭제 시 삭제 건수가 0이면 예외가 발생하지 않는다.")
    void deleteByIdAndName_삭제건수0_성공() {
        when(reservationRepository.deleteByIdAndName(1L, "a")).thenReturn(0);

        assertThatCode(() -> reservationService.deleteByIdAndName(1L, "a"))
                .doesNotThrowAnyException();
        verify(reservationRepository).deleteByIdAndName(1L, "a");
    }

    @Test
    @DisplayName("이름 기준 예약 삭제 시 삭제 건수가 1이면 예외가 발생하지 않는다.")
    void deleteByIdAndName_삭제건수1_성공() {
        when(reservationRepository.deleteByIdAndName(1L, "a")).thenReturn(1);

        assertThatCode(() -> reservationService.deleteByIdAndName(1L, "a"))
                .doesNotThrowAnyException();
        verify(reservationRepository).deleteByIdAndName(1L, "a");
    }

    @Test
    @DisplayName("이름 기준 예약 삭제 시 삭제 건수가 2 이상이면 예외가 발생한다.")
    void deleteByIdAndName_삭제건수2이상_실패() {
        when(reservationRepository.deleteByIdAndName(1L, "a")).thenReturn(2);

        assertThatThrownBy(() -> reservationService.deleteByIdAndName(1L, "a"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("reservationId=1");
        verify(reservationRepository).deleteByIdAndName(1L, "a");
    }
}
