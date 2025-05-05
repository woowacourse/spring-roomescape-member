package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.reservation.business.model.entity.ReservationTime;
import roomescape.reservation.business.model.repository.ReservationDao;
import roomescape.reservation.business.model.repository.ReservationTimeDao;
import roomescape.reservation.business.service.ReservationTimeService;
import roomescape.reservation.presentation.request.ReservationTimeRequest;
import roomescape.reservation.presentation.response.ReservationTimeResponse;

class ReservationTimeServiceTest {

    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationDao = Mockito.mock(ReservationDao.class);
        reservationTimeDao = Mockito.mock(ReservationTimeDao.class);
        reservationTimeService = new ReservationTimeService(reservationDao, reservationTimeDao);
    }

    @Test
    void 예약시간을_추가한다() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(23, 0));
        Mockito.when(reservationTimeDao.existByTime(Mockito.any()))
                .thenReturn(false);

        Mockito.when(reservationTimeDao.save(Mockito.any()))
                .thenReturn(
                        new ReservationTime(1L, LocalTime.of(23, 0))
                );

        ReservationTimeResponse result = reservationTimeService.add(request);

        ReservationTimeResponse expected = ReservationTimeResponse.of(new ReservationTime(1L, LocalTime.of(23, 0)));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void 모든_예약시간을_조회한다() {
        Mockito.when(reservationTimeDao.findAll())
                .thenReturn(
                        List.of(new ReservationTime(1L, LocalTime.of(23, 0)))
                );

        List<ReservationTimeResponse> result = reservationTimeService.findAll();
        List<ReservationTimeResponse> expected = List.of(
                ReservationTimeResponse.of(new ReservationTime(1L, LocalTime.of(23, 0))));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void 예약시간을_삭제한다() {
        Long id = 1L;

        Mockito.when(reservationDao.existByTimeId(Mockito.any()))
                .thenReturn(false);

        Mockito.when(reservationTimeDao.deleteById(Mockito.any()))
                .thenReturn(1);

        assertThatCode(() -> reservationTimeService.deleteById(id)).doesNotThrowAnyException();
    }
}
