package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.repository.ReservationFakeRepository;
import roomescape.domain.repository.ReservationTimeFakeRepository;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;

class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setup() {
        ReservationRepository reservationRepository = new ReservationFakeRepository();
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

        List<Reservation> reservations = List.of(
                new Reservation(null, "루키", LocalDate.of(2025, 3, 28), times.get(0)),
                new Reservation(null, "슬링키", LocalDate.of(2025, 4, 5), times.get(1)),
                new Reservation(null, "범블비", LocalDate.of(2025, 4, 23), times.get(2))
        );

        for (Reservation reservation : reservations) {
            reservationRepository.saveAndReturnId(reservation);
        }

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository);
    }

    @DisplayName("전체 예약 정보를 조회한다")
    @Test
    void get_all_test() {
        // when
        List<ReservationResponse> reservationResponses = reservationService.getAll();

        // then
        assertThat(reservationResponses).hasSize(3);
    }

    @DisplayName("예약 정보를 추가한다")
    @Test
    void add_test() {
        // given
        ReservationRequest request = new ReservationRequest("루키", LocalDate.of(2025, 5, 3), 4L);

        // when
        ReservationResponse response = reservationService.add(request);

        // then
        ReservationResponse expected = new ReservationResponse(4L, "루키", LocalDate.of(2025, 5, 3),
                new ReservationTimeResponse(4L, LocalTime.of(23, 53)));
        assertThat(response).isEqualTo(expected);
    }

    @DisplayName("성공적으로 예약 정보를 삭제하면 예외가 발생하지 않는다")
    @Test
    void remove_test() {
        // given
        Long removeId = 3L;

        // when && then
        assertThatCode(() -> reservationService.remove(removeId))
                .doesNotThrowAnyException();
    }

    @DisplayName("지난 날짜인 경우 예외가 발생한다")
    @Test
    void past_day_exception_test() {
        // given
        ReservationRequest request = new ReservationRequest("루키", LocalDate.now().minusDays(1), 4L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 날짜는 예약할 수 없습니다.");
    }

    @DisplayName("지난 시각인 경우 예외가 발생한다")
    @Test
    void past_time_exception_test() {
        // given
        ReservationRequest request = new ReservationRequest("루키", LocalDate.now(), 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 시각은 예약할 수 없습니다.");
    }

    @DisplayName("미래 날짜인데 시간이 현재보다 과거인 경우 예외가 발생하지 않는다")
    @Test
    void future_test() {
        // given
        ReservationRequest request = new ReservationRequest("루키", LocalDate.now().plusDays(3), 1L);

        // when & then
        assertThatCode(() -> reservationService.add(request))
                .doesNotThrowAnyException();

    }
}
