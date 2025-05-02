package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.fake.FakeReservationTimeDao;

public class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeService = new ReservationTimeService(new FakeReservationTimeDao());
    }

    @Test
    @DisplayName("예약 시간을 추가를 할 수 있다.")
    void addReservation() {
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10, 0));

        ReservationTime actual = reservationTimeService.createReservationTime(request);

        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(1L);
            assertThat(actual.getStartAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 가져올 수 있다.")
    void findAllReservations() {
        ReservationTimeCreateRequest request1 = new ReservationTimeCreateRequest(LocalTime.of(10, 0));
        ReservationTimeCreateRequest request2 = new ReservationTimeCreateRequest(LocalTime.of(12, 0));

        reservationTimeService.createReservationTime(request1);
        reservationTimeService.createReservationTime(request2);

        assertThat(reservationTimeService.findAllReservationTimes()).hasSize(2);
    }

    @Test
    @DisplayName("예약 시간을 id를 통해 제거할 수 있다.")
    void removeReservation() {
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10, 0));
        reservationTimeService.createReservationTime(request);

        reservationTimeService.deleteReservationTimeById(1L);

        assertThat(reservationTimeService.findAllReservationTimes()).hasSize(0);
    }
}
