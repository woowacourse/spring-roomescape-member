package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.request.ReservationTimeCreateRequest;
import roomescape.domain.reservation.response.ReservationTimeResponse;
import roomescape.fake.FakeReservationTimeRepository;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeService = new ReservationTimeService(new FakeReservationTimeRepository());
    }

    @Test
    @DisplayName("예약 시간을 성공적으로 생성한다.")
    void saveReservationTime() {
        // given
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10, 0));

        // when
        ReservationTimeResponse response = reservationTimeService.saveReservationTime(request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("모든 예약 시간을 조회한다.")
    void findAllReservationTimes() {
        // given
        reservationTimeService.saveReservationTime(new ReservationTimeCreateRequest(LocalTime.of(10, 0)));
        reservationTimeService.saveReservationTime(new ReservationTimeCreateRequest(LocalTime.of(11, 0)));

        // when
        List<ReservationTimeResponse> responses = reservationTimeService.findAllReservationTimes();

        // then
        assertThat(responses).hasSize(2)
                .extracting("startAt")
                .containsExactly(LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteReservationTimeBy() {
        // given
        ReservationTimeResponse savedTime = reservationTimeService.saveReservationTime(
                new ReservationTimeCreateRequest(LocalTime.of(10, 0))
        );

        // when
        reservationTimeService.deleteReservationTimeBy(savedTime.id());

        // then
        List<ReservationTimeResponse> responses = reservationTimeService.findAllReservationTimes();
        assertThat(responses).isEmpty();
    }
}
