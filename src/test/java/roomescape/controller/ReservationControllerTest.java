package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.fixture.ReservationServiceStub;

class ReservationControllerTest {
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        reservationController = new ReservationController(new ReservationServiceStub());
    }

    @DisplayName("GET /reservations 요청시 200 상태코드와 응답본문에 존재하는 예약 정보를 받는다")
    @Test
    void getReservations() {
        // given
        String name = "에드";
        LocalDate date = LocalDate.now();
        Long timeId = 1L;
        ReservationRequest request = new ReservationRequest(name, date, timeId);
        reservationController.createReservation(request);

        // when
        ResponseEntity<List<ReservationResponse>> response = reservationController.getReservations();
        ReservationResponse reservationResponse = response.getBody().getFirst();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(reservationResponse.getName()).isEqualTo(name);
        assertThat(reservationResponse.getDate()).isEqualTo(date);
        assertThat(reservationResponse.getTime().getId()).isEqualTo(timeId);
    }

    @DisplayName("POST /reservations 요청시 201 상태코드와 생성된 예약 정보를 받는다")
    @Test
    void createReservation() {
        // given
        String name = "에드";
        LocalDate date = LocalDate.now();
        Long timeId = 1L;
        ReservationRequest request = new ReservationRequest(name, date, timeId);

        // when
        ResponseEntity<ReservationResponse> response = reservationController.createReservation(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(name);
        assertThat(response.getBody().getDate()).isEqualTo(date);
        assertThat(response.getBody().getTime().getId()).isEqualTo(timeId);
    }

    @DisplayName("DELETE /reservations/{id} 요청시 200 상태코드를 받는다")
    @Test
    void deleteReservation() {
        // given
        String name = "에드";
        LocalDate date = LocalDate.now();
        Long timeId = 1L;
        ReservationRequest request = new ReservationRequest(name, date, timeId);
        reservationController.createReservation(request);

        // when
        ResponseEntity<Void> response = reservationController.deleteReservation(timeId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
