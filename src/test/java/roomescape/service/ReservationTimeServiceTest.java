package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.service.fake_dao.FakeReservationReservationTimeDao;

class ReservationTimeServiceTest {

    private final ReservationTimeDao timeDao = new FakeReservationReservationTimeDao();
    private final ReservationTimeService timeService = new ReservationTimeService(timeDao);

    @Test
    void createReservationTime() {
        LocalTime startAt = LocalTime.of(10, 0, 0);
        ReservationTimeResponseDto time = timeService.createTime(
                new ReservationTimeRequestDto(startAt)
        );

        assertThat(time.startAt()).isEqualTo(startAt);
    }

    @Test
    void findAllReservationTimes() {
        createReservationTime();

        List<ReservationTimeResponseDto> times = timeService.findAllTimes();

        assertThat(times).hasSize(1);
    }

    @Test
    void deleteTime() {
        createReservationTime();

        timeService.deleteTime(1L);

        assertThatThrownBy(() -> timeDao.findById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
