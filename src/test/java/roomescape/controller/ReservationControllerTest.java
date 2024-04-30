package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.ReservationInput;
import roomescape.service.dto.ReservationTimeInput;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationControllerTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationTimeService reservationTimeService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("예약 생성에 성공하면, 201을 반환한다")
    void return_200_when_reservation_create_success() {
        long id = reservationTimeService.createReservationTime(new ReservationTimeInput("10:00")).id();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", id);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력되었을 때 400을 반환한다.")
    void return_400_when_reservation_create_input_is_invalid() {
        long id = reservationTimeService.createReservationTime(new ReservationTimeInput("10:00")).id();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", id);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("특정 예약이 존재하지 않는데, 그 예약을 삭제하려 할 때 404을 반환한다.")
    void return_404_when_not_exist_id() {
        RestAssured.given()
                .delete("/reservations/-1")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("중복된 예약을 생성하려 할 때 409를 반환한다.")
    void return_409_when_duplicate_reservation() {
        long id = reservationTimeService.createReservationTime(new ReservationTimeInput("10:00")).id();
        reservationService.createReservation(new ReservationInput("조이썬", "2024-04-30", id));

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "제리");
        reservation.put("date", "2024-04-30");
        reservation.put("timeId", id);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then()
                .statusCode(409);
    }
}
