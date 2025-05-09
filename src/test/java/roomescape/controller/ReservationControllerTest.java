/*
package roomescape.controller;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.service.ReservationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @MockitoBean
    private ReservationService reservationService;

    @DisplayName("예약 조회 api 테스트")
    @Test
    void get_reservations() {
        given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200);
    }

    @DisplayName("예약 상황 포함 조회 api 테스트")
    @Test
    void get_booked_reservations() {
        given().log().all()
            .when().get("/reservations/dates/2025-05-01/themes/1/times")
            .then().log().all()
            .statusCode(200);
    }

    @DisplayName("예약 추가 api 테스트")
    @Test
    void post_reservation() {
        ReservationRequestDto request = new ReservationRequestDto(
            "젠슨",
            "2025-05-05",
            1L,
            1L);

        given()
            .contentType("application/json")
            .body(request)
            .when()
            .post("/reservations")
            .then()
            .statusCode(201);
        Mockito.verify(reservationService).saveReservation(request);
    }

    @DisplayName("예약 삭제 api 테스트")
    @Test
    void delete_reservation() {

        given()
            .contentType("application/json")
            .when().delete("/reservations/1")
            .then().statusCode(204);
        Mockito.verify(reservationService).deleteReservation(1L);
    }
}
*/
