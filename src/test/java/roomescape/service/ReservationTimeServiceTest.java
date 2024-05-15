package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private ReservationTimeDao reservationTimeDao;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("새로운 예약 시간을 저장할 수 있다")
    void should_SaveNewTime() {
        //given
        LocalTime requestTime = LocalTime.of(9, 0, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(requestTime);
        ReservationTime response = new ReservationTime(1L, requestTime);
        Mockito.when(reservationTimeDao.save(any(ReservationTime.class)))
                .thenReturn(response);

        //when
        ReservationTimeResponse savedTime = reservationTimeService.save(request);

        //then
        assertThat(savedTime.id()).isEqualTo(1L);
        assertThat(savedTime.startAt()).isEqualTo(requestTime);
    }

    @Test
    @DisplayName("모든 예약시간 리스트를 찾을 수 있다")
    void should_FindAllTimes() {
        //given
        int expectedSize = 1;
        LocalTime requestTime = LocalTime.of(9, 0, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(requestTime);
        ReservationTime response = new ReservationTime(1L, requestTime);
        Mockito.when(reservationTimeDao.getAll())
                .thenReturn(List.of(response));

        //when
        List<ReservationTimeResponse> times = reservationTimeService.findAll();

        //then
        assertThat(times).hasSize(expectedSize);
    }

    @Test
    @DisplayName("성공 : 예약시간을 사용중인 예약이 없고 예약시간이 db에 존재한다면 시간 삭제에 성공한다")
    void should_SuccessDeleteTime_When_NoOtherReservationUsingTime_And_TimeExistInDb() {
        //given
        ReservationTime dummy = new ReservationTime(null, LocalTime.now());
        ArgumentCaptor<Long> valueCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.when(reservationDao.findByTimeId(anyLong()))
                .thenReturn(List.of());
        Mockito.when(reservationTimeDao.findById(anyLong()))
                .thenReturn(Optional.of(dummy));
        Mockito.doNothing()
                .when(reservationTimeDao).delete(valueCaptor.capture());

        //when
        reservationTimeService.delete(1L);

        //then
        Mockito.verify(reservationTimeDao, times(1)).delete(anyLong());
        assertThat(valueCaptor.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("실패 : 예약시간을 사용중인 예약이 있다면 시간 삭제에 실패한다")
    void should_ThrowIllegalArgumentException_When_ReservationUsingTime() {
        //given
        List<Reservation> dummyReservations = List.of(Mockito.mock(Reservation.class));
        Mockito.when(reservationDao.findByTimeId(anyLong()))
                .thenReturn(dummyReservations);

        //when - then
        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패 : 예약시간이 DB에 존재하지 않는다면 시간 삭제에 실패한다")
    void should_ThrowNoSuchElementException_When_TryDeleteNotExistTime() {
        //given
        Mockito.when(reservationTimeDao.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        //when - then
        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}