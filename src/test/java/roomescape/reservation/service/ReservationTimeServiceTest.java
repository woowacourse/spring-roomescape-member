package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.repository.ReservationTimeFakeRepository;
import roomescape.reservation.controller.dto.ReservationTimeRequest;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.ReservationTimeRepository;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setup() {
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();

        List<ReservationTime> times = List.of(
                new ReservationTime(null, LocalTime.of(3, 12)),
                new ReservationTime(null, LocalTime.of(11, 33)),
                new ReservationTime(null, LocalTime.of(16, 54)),
                new ReservationTime(null, LocalTime.of(23, 53))
        );

        for (ReservationTime time : times) {
            reservationTimeRepository.saveAndReturnId(time);
        }

        reservationTimeService = new ReservationTimeService(reservationTimeRepository);
    }

    @DisplayName("에약 시간 정보를 추가한다")
    @Test
    void add_test() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(17, 25));

        // when
        ReservationTimeResponse response = reservationTimeService.add(request);

        // then
        ReservationTimeResponse expected = new ReservationTimeResponse(5L, LocalTime.of(17, 25));
        assertThat(response).isEqualTo(expected);
    }

    @DisplayName("예약 시간 정보 정상적으로 삭제하면 예외가 발생하지 않는다")
    @Test
    void remove_test() {
        // given
        Long removeId = 3L;

        // when && then
        assertThatCode(() -> reservationTimeService.remove(removeId))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약 정보 시간을 모두 조회한다")
    @Test
    void get_times_test() {
        // when
        List<ReservationTimeResponse> reservationTimeRespons = reservationTimeService.getTimes();

        // then
        assertThat(reservationTimeRespons).hasSize(4);
    }

}
