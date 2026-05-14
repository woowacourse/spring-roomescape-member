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
import roomescape.exception.CustomException;
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
    void 시간_삭제_성공() {
        // given
        Long reservationTimeId = 1L;
        ReservationTime reservationTime = new ReservationTime(reservationTimeId, LocalTime.of(10, 0));

        when(reservationTimeQueryingDao.findReservationTimeById(reservationTimeId))
                .thenReturn(Optional.of(reservationTime));

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

        when(reservationTimeQueryingDao.findReservationTimeById(reservationTimeId))
                .thenReturn(Optional.empty());

        // when && then
        Assertions.assertThrows(CustomException.class, () -> reservationTimeService.delete(reservationTimeId));
    }

    @Test
    void 시간_삭제_에러_예약이_있는_시간() {
        // given
        Long reservationTimeId = 1L;
        ReservationTime reservationTime = new ReservationTime(reservationTimeId, LocalTime.of(10, 0));

        when(reservationTimeQueryingDao.findReservationTimeById(reservationTimeId))
                .thenReturn(Optional.of(reservationTime));

        when(reservationQueryingDao.existsReservationByTimeId(reservationTimeId))
                .thenReturn(true);

        // when && then
        Assertions.assertThrows(IllegalArgumentException.class, () -> reservationTimeService.delete(reservationTimeId));
    }

}
