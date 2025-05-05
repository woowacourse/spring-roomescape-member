package roomescape.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import roomescape.application.dto.ReservationTimeRequest;
import roomescape.application.dto.ReservationTimeResponse;
import roomescape.application.service.fake_dao.FakeReservationDao;
import roomescape.application.service.fake_dao.FakeReservationTimeDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;

class ReservationTimeServiceTest {

    private final ReservationTimeDao reservationTimeDao = new FakeReservationTimeDao();
    private final ReservationDao reservationDao = new FakeReservationDao();
    private final ReservationTimeService timeService = new ReservationTimeService(reservationDao,
            reservationTimeDao);

    @Test
    void createReservationTime() {
        LocalTime startAt = LocalTime.of(10, 0, 0);
        ReservationTimeResponse time = timeService.createReservationTime(
                new ReservationTimeRequest(startAt)
        );

        assertThat(time.startAt()).isEqualTo(startAt);
    }

    @Test
    void findAllReservationTimes() {
        createReservationTime();

        List<ReservationTimeResponse> times = timeService.findAllReservationTimes();

        assertThat(times).hasSize(1);
    }

    @Test
    @Disabled
    void deleteTime() {
        createReservationTime();

        timeService.deleteReservationTime(1L);

        assertThatThrownBy(() -> reservationTimeDao.findById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
