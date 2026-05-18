package roomescape.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeUpdateRequest;
import roomescape.exception.BusinessException;
import roomescape.repository.ReservationQueryingDao;
import roomescape.repository.ReservationTimeQueryingDao;
import roomescape.repository.ReservationTimeUpdatingDao;
import roomescape.service.ReservationTimeService;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    ReservationTimeUpdatingDao reservationTimeUpdatingDao;

    @Mock
    ReservationTimeQueryingDao reservationTimeQueryingDao;

    @Mock
    ReservationQueryingDao reservationQueryingDao;

    @InjectMocks
    ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 시간을 생성할 수 있다.")
    void 시간_생성_성공() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(time);

        when(reservationTimeQueryingDao.existsByStartAt(time))
                .thenReturn(false);

        // when
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(request);

        // then
        Assertions.assertEquals(time, reservationTimeResponse.getStartAt());
    }

    @Test
    @DisplayName("예약 시간을 생성할 때 이미 존재하는 시간인 경우 에러가 발생한다.")
    void 시간_생성_에러_중복_시간() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(time);

        when(reservationTimeQueryingDao.existsByStartAt(time))
                .thenReturn(true);

        // when && then
        Assertions.assertThrows(BusinessException.class, () -> reservationTimeService.create(request));
        verify(reservationTimeUpdatingDao, never()).insert(request);
    }

    @Test
    @DisplayName("예약 시간 목록을 조회할 수 있다.")
    void 시간_조회_성공() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        ReservationTime reservationTime = new ReservationTime(1L, time);

        when(reservationTimeQueryingDao.findAllReservationTime())
                .thenReturn(List.of(reservationTime));

        // when
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.read(null, null);

        // then
        Assertions.assertEquals(time, reservationTimeResponses.getFirst().getStartAt());
    }

    @Test
    @DisplayName("예약 시간을 수정할 수 있다.")
    void 시간_수정_성공() {
        // given
        Long reservationTimeId = 1L;
        ReservationTimeUpdateRequest request = new ReservationTimeUpdateRequest(LocalTime.now());
        ReservationTime reservationTime = new ReservationTime(reservationTimeId, request.getStartAt());

        when(reservationTimeQueryingDao.existsById(reservationTimeId))
                .thenReturn(true);

        when(reservationTimeQueryingDao.findReservationTimeById(reservationTimeId))
                .thenReturn(Optional.of(reservationTime));

        // when
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.update(reservationTimeId, request);

        // then
        Assertions.assertEquals(request.getStartAt(), reservationTimeResponse.getStartAt());
        verify(reservationTimeUpdatingDao, times(1)).update(reservationTimeId, request);
    }

    @Test
    @DisplayName("예약 시간을 수정할 때 존재하지 않는 예약 시간인 경우 에러가 발생한다.")
    void 시간_수정_에러_시간_없음() {
        // given
        Long reservationTimeId = 1L;
        ReservationTimeUpdateRequest request = new ReservationTimeUpdateRequest(LocalTime.now());

        when(reservationTimeQueryingDao.existsById(reservationTimeId))
                .thenReturn(false);

        // when && then
        Assertions.assertThrows(BusinessException.class, () -> reservationTimeService.update(reservationTimeId, request));
        verify(reservationTimeQueryingDao, never()).findReservationTimeById(reservationTimeId);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다.")
    void 시간_삭제_성공() {
        // given
        Long reservationTimeId = 1L;

        when(reservationQueryingDao.existsReservationByTimeId(reservationTimeId))
                .thenReturn(false);

        // when
        reservationTimeService.delete(reservationTimeId);

        // then
        verify(reservationTimeUpdatingDao, times(1)).delete(reservationTimeId);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 때 예약이 있는 시간인 경우 에러가 발생한다.")
    void 시간_삭제_에러_예약이_있는_시간() {
        // given
        Long reservationTimeId = 1L;

        when(reservationQueryingDao.existsReservationByTimeId(reservationTimeId))
                .thenReturn(true);

        // when && then
        Assertions.assertThrows(BusinessException.class, () -> reservationTimeService.delete(reservationTimeId));
    }
}
