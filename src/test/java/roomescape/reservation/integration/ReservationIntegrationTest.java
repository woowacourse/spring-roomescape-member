package roomescape.reservation.integration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.reservation.dto.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data-test.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("정상적인 요청에 대하여 예약을 정상적으로 등록, 조회, 삭제한다.")
    void adminReservationPageWork() {
        ReservationRequest reservationRequest = new ReservationRequest(LocalDate.now(), "polla", 1L, 1L);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when()
                .post("/reservations")
                .then()
                .statusCode(201);

        RestAssured.given()
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(3));

        RestAssured.given()
                .when()
                .delete("/reservations/3")
                .then()
                .statusCode(204);

        RestAssured.given()
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("예약을 요청시 존재하지 않은 예약 시간의 id일 경우 예외가 발생한다.")
    void notExistTime() {
        ReservationRequest reservationRequest = new ReservationRequest(LocalDate.now(), "polla", 99L, 1L);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when()
                .post("/reservations")
                .then()
                .statusCode(500);
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 조회한다.")
    void findReservationTimeList() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/reservations/times/1?date=" + LocalDate.now())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약을 요청시 예약자명이 존재하지 않으면 예외가 발생한다.")
    void saveReservation_ShouldReturnBADREQUEST_WhenNameIsNull() {
        ReservationRequest reservationRequest = new ReservationRequest(LocalDate.now(), null, 1L, 1L);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when()
                .post("/reservations")
                .then()
                .statusCode(400)
                .body(containsString("예약자명이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("예약을 요청시 예약 일자가 존재하지 않으면 예외가 발생한다.")
    void saveReservation_ShouldReturnBADREQUEST_WhenDateIsNull() {
        ReservationRequest reservationRequest = new ReservationRequest(null, "Dobby", 1L, 1L);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when()
                .post("/reservations")
                .then()
                .statusCode(400)
                .body(containsString("날짜가 존재하지 않습니다."));
    }

}
