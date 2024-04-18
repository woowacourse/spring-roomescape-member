package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeAddRequest;
import roomescape.repository.ReservationTimeDao;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceMockTest {

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Mock
    private ReservationTimeDao reservationTimeDao;

    private ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

    @DisplayName("모든 예약 시간을 찾습니다")
    @Test
    void findAllReservationTime() {
        given(reservationTimeDao.findAll()).willReturn(List.of(reservationTime));
        int expectedSize = 1;

        int actualSize = reservationTimeService.findAllReservationTime().size();

        assertThat(actualSize).isEqualTo(expectedSize);
    }

    @DisplayName("예약시간을 추가하고 저장된 예약시간을 반환합니다.")
    @Test
    void should_add_reservation_time() {
        ReservationTimeAddRequest reservationTimeAddRequest = new ReservationTimeAddRequest(
                LocalTime.of(10, 0)
        );
        given(reservationTimeDao.insert(reservationTimeAddRequest)).willReturn(reservationTime);

        ReservationTime actualReservationTime = reservationTimeService.addReservationTime(reservationTimeAddRequest);

        assertThat(actualReservationTime).isEqualTo(reservationTime);
    }

    @DisplayName("원하는 id의 예약시간을 삭제합니다")
    @Test
    void should_remove_reservation_time_with_exist_id() {
        given(reservationTimeDao.findById(1L)).willReturn(Optional.of(reservationTime));
        doNothing().when(reservationTimeDao).deleteById(1L);

        reservationTimeService.removeReservationTime(1L);

        verify(reservationTimeDao, times(1)).deleteById(1L);
    }

    @DisplayName("없는 id의 예약시간을 삭제하면 예외를 발생합니다.")
    @Test
    void should_throw_exception_when_remove_reservation_time_with_non_exist_id() {
        given(reservationTimeDao.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationTimeService.removeReservationTime(1L)).isInstanceOf(
                        IllegalArgumentException.class)
                .hasMessage("해당 id를 가진 예약시간이 존재하지 않습니다.");
    }
}
