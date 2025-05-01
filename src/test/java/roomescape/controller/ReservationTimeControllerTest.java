//package roomescape.controller;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.time.LocalTime;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import roomescape.dto.reservationtime.ReservationTimeRequest;
//import roomescape.dto.reservationtime.ReservationTimeResponse;
//import roomescape.fixture.ReservationTimeServiceStub;
//
//class ReservationTimeControllerTest {
//    private ReservationTimeController reservationTimeController;
//
//    @BeforeEach
//    void setUp() {
//        reservationTimeController = new ReservationTimeController(new ReservationTimeServiceStub());
//    }
//
//    @DisplayName("GET /times 요청시 200 상태코드와 응답본문에 존재하는 예약 시간 정보를 받는다")
//    @Test
//    void getReservationTimes() {
//        // given
//        LocalTime startAt = LocalTime.now();
//        ReservationTimeRequest request = new ReservationTimeRequest(startAt);
//        reservationTimeController.createReservationTime(request);
//
//        // when
//        ResponseEntity<List<ReservationTimeResponse>> response = reservationTimeController.getReservationTimes();
//        ReservationTimeResponse reservationTimeResponse = response.getBody().getFirst();
//
//        // then
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).hasSize(1);
//        assertThat(reservationTimeResponse.getStartAt()).isEqualTo(startAt);
//    }
//
//    @DisplayName("POST /times 요청시 201 상태코드와 생성된 예약 시간 정보를 받는다")
//    @Test
//    void createReservationTime() {
//        // given
//        LocalTime startAt = LocalTime.now();
//        ReservationTimeRequest request = new ReservationTimeRequest(startAt);
//
//        // when
//        ResponseEntity<ReservationTimeResponse> response = reservationTimeController.createReservationTime(request);
//
//        // then
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(response.getBody().getStartAt()).isEqualTo(startAt);
//    }
//
//    @DisplayName("DELETE /times/{id} 요청시 204 상태코드를 받는다")
//    @Test
//    void deleteReservationTime() {
//        // given
//        LocalTime startAt = LocalTime.now();
//        ReservationTimeRequest request = new ReservationTimeRequest(startAt);
//        reservationTimeController.createReservationTime(request);
//
//        // when
//        ResponseEntity<Void> response = reservationTimeController.deleteReservationTime(1L);
//
//        // then
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//    }
//}
