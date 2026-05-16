package roomescape.service;

import org.junit.jupiter.api.Assertions;
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
    void 시간_생성_성공() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(time);

        // when
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(request);

        // then
        Assertions.assertEquals(time, reservationTimeResponse.getStartAt());
    }

    @Test
    void 시간_조회_성공() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        ReservationTime reservationTime = new ReservationTime(1L, time);

        // when
        when(reservationTimeQueryingDao.findAllReservationTime(null, null))
                .thenReturn(List.of(reservationTime));
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.read(null, null);

        // then
        Assertions.assertEquals(time, reservationTimeResponses.getFirst().getStartAt());
    }

    @Test
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
    void 시간_삭제_성공() {
        // given
        Long reservationTimeId = 1L;

        when(reservationTimeQueryingDao.existsById(reservationTimeId))
                .thenReturn(true);

        when(reservationQueryingDao.existsReservationByTimeId(reservationTimeId))
                .thenReturn(false);

        // when
        reservationTimeService.delete(reservationTimeId);

        // then
        verify(reservationTimeUpdatingDao, times(1)).delete(reservationTimeId);
    }

    @Test
    void 시간_삭제_에러_존재하지_않는_시간() {
        // given
        Long reservationTimeId = 1L;

        when(reservationTimeQueryingDao.existsById(reservationTimeId))
                .thenReturn(false);

        // when && then
        Assertions.assertThrows(BusinessException.class, () -> reservationTimeService.delete(reservationTimeId));
        verify(reservationQueryingDao, never()).existsReservationByTimeId(reservationTimeId);
    }

    @Test
    void 시간_삭제_에러_예약이_있는_시간() {
        // given
        Long reservationTimeId = 1L;

        when(reservationTimeQueryingDao.existsById(reservationTimeId))
                .thenReturn(true);

        when(reservationQueryingDao.existsReservationByTimeId(reservationTimeId))
                .thenReturn(true);

        // when && then
        Assertions.assertThrows(BusinessException.class, () -> reservationTimeService.delete(reservationTimeId));
    }

}
