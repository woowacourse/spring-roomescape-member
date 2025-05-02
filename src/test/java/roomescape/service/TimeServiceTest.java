package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeCreateRequest;
import roomescape.fake.FakeReservationTimeDao;

public class TimeServiceTest {

    private TimeService timeService;

    @BeforeEach
    void setUp() {
        timeService = new TimeService(new FakeReservationTimeDao());
    }

    @Test
    @DisplayName("예약 시간을 추가를 할 수 있다.")
    void addReservation() {
        TimeCreateRequest request = new TimeCreateRequest(LocalTime.of(10, 0));

        ReservationTime actual = timeService.createReservationTime(request);

        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(1L);
            assertThat(actual.getStartAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 가져올 수 있다.")
    void findAllReservations() {
        TimeCreateRequest request1 = new TimeCreateRequest(LocalTime.of(10, 0));
        TimeCreateRequest request2 = new TimeCreateRequest(LocalTime.of(12, 0));

        timeService.createReservationTime(request1);
        timeService.createReservationTime(request2);

        assertThat(timeService.findAllReservationTimes()).hasSize(2);
    }

    @Test
    @DisplayName("예약 시간을 id를 통해 제거할 수 있다.")
    void removeReservation() {
        TimeCreateRequest request = new TimeCreateRequest(LocalTime.of(10, 0));
        timeService.createReservationTime(request);

        timeService.deleteReservationTimeById(1L);

        assertThat(timeService.findAllReservationTimes()).hasSize(0);
    }
}
