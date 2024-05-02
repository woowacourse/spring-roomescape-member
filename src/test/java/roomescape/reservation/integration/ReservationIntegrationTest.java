package roomescape.reservation.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.reservation.dto.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data-test.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
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

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));

        RestAssured.given().log().all()
                .when().delete("/reservations/3")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("예약을 요청시 존재하지 않은 예약 시간의 id일 경우 예외가 발생한다.")
    void notExistTime() {
        ReservationRequest reservationRequest = new ReservationRequest(LocalDate.now(), "polla", 99L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(500);
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 조회한다.")
    void findReservationTimeList() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations/1?date=" + LocalDate.now())
                .then().log().all()
                .statusCode(200);
    }
}
