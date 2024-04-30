package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.reservation.ReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.controller.time.TimeResponse;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                new ReservationFakeRepository(),
                new ReservationTimeFakeRepository()
        );
    }

    @Test
    @DisplayName("예약 목록을 조회한다.")
    void getReservations() {
        // given
        List<ReservationResponse> expected = List.of(
                new ReservationResponse(1L, "al", "2024-01-20", new TimeResponse(1L, "10:15")),
                new ReservationResponse(2L, "be", "2024-02-19", new TimeResponse(2L, "11:20"))
        );

        // when
        List<ReservationResponse> actual = reservationService.getReservations();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void addReservation() {
        // given
        ReservationRequest request = new ReservationRequest("cha", LocalDate.of(2024, 3, 18), 3L);
        ReservationResponse expected = new ReservationResponse(3L, "cha", "2024-03-18", new TimeResponse(3L, "12:25"));

        // when
        ReservationResponse actual = reservationService.addReservation(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하는 예약을 삭제한다.")
    void deleteReservationPresent() {
        // given
        Long id = 2L;

        // when & then
        assertThat(reservationService.deleteReservation(id)).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제한다.")
    void deleteReservationNotPresent() {
        // given
        Long id = 3L;

        // when & then
        assertThat(reservationService.deleteReservation(id)).isEqualTo(0);
    }
}
