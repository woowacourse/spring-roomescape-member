package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.request.ReservationTimePostRequest;
import roomescape.dto.response.ReservationTimePostResponse;
import roomescape.service.fake_dao.FakeReservationDao;
import roomescape.service.fake_dao.FakeReservationTimeDao;

class ReservationTimeServiceTest {

    private final ReservationTimeDao reservationTimeDao = new FakeReservationTimeDao();
    private final ReservationDao reservationDao = new FakeReservationDao();
    private final ReservationTimeQueryService timeQueryService = new ReservationTimeQueryService(reservationDao,
            reservationTimeDao);
    private final ReservationTimeCommandService timeCommandService = new ReservationTimeCommandService(reservationDao,
            reservationTimeDao);

    @Test
    void createReservationTime() {
        LocalTime startAt = LocalTime.of(10, 0, 0);
        ReservationTimePostResponse time = timeCommandService.createTime(
                new ReservationTimePostRequest(startAt)
        );

        assertThat(time.startAt()).isEqualTo(startAt);
    }

    @Test
    void findAllReservationTimes() {
        createReservationTime();

        List<ReservationTimePostResponse> times = timeQueryService.findAllTimes();

        assertThat(times).hasSize(1);
    }

    @Test
    @Disabled
    void deleteTime() {
        createReservationTime();

        timeCommandService.deleteTime(1L);

        assertThatThrownBy(() -> reservationTimeDao.findById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
