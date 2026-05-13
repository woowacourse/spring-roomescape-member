package roomescape.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRequest;
import roomescape.domain.reservationtime.ReservationTimeResponse;
import roomescape.repository.ReservationTimeQueryingDao;
import roomescape.repository.ReservationTimeUpdatingDao;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    ReservationTimeUpdatingDao reservationTimeUpdatingDao;

    @Mock
    ReservationTimeQueryingDao reservationTimeQueryingDao;

    @InjectMocks
    ReservationTimeService reservationTimeService;

    @Test
    void 시간_생성_성공() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(time);

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
}
