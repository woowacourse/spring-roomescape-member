package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import roomescape.dao.ReservationDao;
import roomescape.fake.FakeReservaionDao;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ReservationTimeServiceTest {
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        ReservationTimeDao reservationTimeDao = new FakeReservationTimeDao();
        ReservationDao reservationDao = new FakeReservaionDao();
        reservationTimeService = new ReservationTimeService(reservationDao, reservationTimeDao);
    }

    @Test
    void 예약시간을_정상적으로_추가() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeResponse response = reservationTimeService.addTime(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0).toString());
    }

    @Test
    void 예약시간_리스트_정상적으로_조회() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        reservationTimeService.addTime(request);

        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getReservationTimes();

        assertThat(reservationTimes).hasSize(1);
    }

    @Test
    void 예약시간을_정상적으로_삭제() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeResponse saved = reservationTimeService.addTime(request);
        Long id = saved.id();

        reservationTimeService.deleteTime(id);
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getReservationTimes();

        assertThat(reservationTimes).isEmpty();
    }

    @Test
    void 삭제하려는_예약시간이_존재하지_않으면_예외() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(999L))
                .isInstanceOf(ResponseStatusException.class);
    }
}
