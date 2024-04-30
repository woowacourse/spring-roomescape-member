package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.request.ReservationTimeRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.handler.exception.CustomException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @DisplayName("예약시간이 없는 경우 예외가 발생한다.")
    @Test
    void reservationTimeIsNotExist() {
        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L);

        assertThatThrownBy(() -> reservationService.createReservation(reservationRequest))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("예약 생성 테스트")
    @Test
    void createReservation() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 1));
        reservationTimeService.createReservationTime(reservationTimeRequest);

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L);
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest);

        assertAll(
                () -> assertThat(reservationResponse.name()).isEqualTo("브라운"),
                () -> assertThat(reservationResponse.date()).isEqualTo(LocalDate.of(2999, 8, 5))
        );
    }

    @DisplayName("모든 예약 조회 테스트")
    @Test
    void findAllReservations() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 1));
        reservationTimeService.createReservationTime(reservationTimeRequest);

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L);
        reservationService.createReservation(reservationRequest);

        List<ReservationResponse> reservations = reservationService.findAllReservations();

        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservations.get(0).name()).isEqualTo("브라운"),
                () -> assertThat(reservations.get(0).date()).isEqualTo(LocalDate.of(2999, 8, 5))
        );
    }

    @DisplayName("예약 삭제 테스트")
    @Test
    void deleteReservation() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 1));
        reservationTimeService.createReservationTime(reservationTimeRequest);

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L);
        ReservationResponse savedReservation = reservationService.createReservation(reservationRequest);

        reservationService.deleteReservation(savedReservation.id());

        List<ReservationResponse> reservations = reservationService.findAllReservations();

        assertThat(reservations).isEmpty();
    }
}
