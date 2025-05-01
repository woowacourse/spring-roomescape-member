package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.repository.ReservationDao;
import roomescape.business.model.repository.ReservationTimeDao;
import roomescape.business.service.ReservationTimeService;
import roomescape.presentation.dto.request.ReservationTimeRequest;
import roomescape.presentation.dto.response.ReservationTimeResponse;

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

        Mockito.when(reservationTimeDao.findById(Mockito.any()))
                .thenReturn(Optional.of(new ReservationTime(1L, LocalTime.of(23, 0))));

        assertThatCode(() -> reservationTimeService.deleteById(id)).doesNotThrowAnyException();
    }
}
