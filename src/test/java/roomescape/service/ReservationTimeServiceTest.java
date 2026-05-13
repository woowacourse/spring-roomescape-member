package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    private static final long TIME_ID = 1L;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약이 존재하는 경우 예약 시간을 삭제할 수 없다")
    void throwException_WhenReservationAlreadyExist() {
        given(reservationTimeRepository.existsByTimeId(TIME_ID)).willReturn(true);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(TIME_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간의 예약이 존재합니다.");

        verify(reservationTimeRepository, never()).deleteTime(TIME_ID);
    }

    @Test
    @DisplayName("예약이 존재하지 않는 경우 예약 시간을 삭제한다")
    void deleteReservationTime_WhenReservationDoesNotExist() {
        given(reservationTimeRepository.existsByTimeId(TIME_ID)).willReturn(false);

        reservationTimeService.deleteReservationTime(TIME_ID);

        verify(reservationTimeRepository).deleteTime(TIME_ID);
    }
}
