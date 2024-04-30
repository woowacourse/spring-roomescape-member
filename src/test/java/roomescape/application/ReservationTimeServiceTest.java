package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @DisplayName("예약 시간 생성을 요청하면 예약 시간 응답을 반환한다.")
    @Test
    void shouldReturnReservationTimeResponseWhenCreateReservationTime() {
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(startAt);
        ReservationTime reservationTime = new ReservationTime(startAt);
        given(reservationTimeRepository.create(reservationTime))
                .willReturn(new ReservationTime(1L, startAt));

        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(reservationTimeRequest);

        then(reservationTimeRepository).should(times(1)).create(reservationTime);
        assertThat(reservationTimeResponse.id()).isEqualTo(1L);
        assertThat(reservationTimeResponse.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("이미 존재하는 예약 시간을 생성 요청하면 IllegalStateException 예외가 발생한다.")
    @Test
    void shouldThrowsIllegalStateExceptionWhenCreateExistStartAtTime() {
        LocalTime startAt = LocalTime.of(10, 0);
        given(reservationTimeRepository.existsByStartAt(startAt))
                .willReturn(true);

        assertThatCode(() -> reservationTimeService.create(new ReservationTimeRequest(LocalTime.of(10, 0))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("이미 존재하는 예약시간이 있습니다. 해당 시간:%s", startAt));
    }

    @DisplayName("예약 시간 조회를 요청하면 저장되어있는 모든 예약 시간대를 반환한다.")
    @Test
    void shouldReturnAllReservationTimesWhenFindAll() {
        given(reservationTimeRepository.findAll())
                .willReturn(List.of(
                        new ReservationTime(1L, LocalTime.of(10, 0)),
                        new ReservationTime(2L, LocalTime.of(11, 0))
                ));

        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();

        then(reservationTimeRepository).should(times(1)).findAll();
        assertThat(reservationTimes).containsExactly(
                new ReservationTimeResponse(1L, LocalTime.of(10, 0)),
                new ReservationTimeResponse(2L, LocalTime.of(11, 0))
        );
    }

    @DisplayName("예약 삭제 요청을 하면, 해당 예약이 저장되어있는지 확인 후 존재하면 삭제한다.")
    @Test
    void shouldDeleteReservationWhenDeleteById() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(reservationTime));
        given(reservationRepository.findReservationCountByTimeId(1L)).willReturn(0L);

        reservationTimeService.deleteById(1L);
        then(reservationTimeRepository).should(times(1)).deleteById(1L);
    }

    @DisplayName("예약에 사용된 예약 시간을 삭제 요청하면, IllegalStateException 예외가 발생한다.")
    @Test
    void shouldThrowsExceptionReservationWhenReservedInTime() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(reservationTime));
        given(reservationRepository.findReservationCountByTimeId(1L)).willReturn(1L);

        assertThatCode(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("해당 예약 시간에 연관된 예약이 존재하여 삭제할 수 없습니다. 삭제 요청한 시간:%s",
                        reservationTime.getStartAt()));
    }

    @DisplayName("존재하지 않는 예약 시간을 삭제 요청하면, IllegalArgumentException 예외가 발생한다.")
    @Test
    void shouldThrowsIllegalArgumentExceptionWhenReservationTimeDoesNotExist() {
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.empty());

        assertThatCode(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간 입니다.");
    }
}
