package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.request.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("예약 추가 API 테스트")
    @Test
    void createReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("예약 조회 API 테스트")
    @Test
    void getReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @DisplayName("예약 취소 API 테스트")
    @Test
    void deleteReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));
        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @DisplayName("올바르지 않은 예약자명 형식으로 입력시 예외처리")
    @Test
    void invalidNameFormat() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));

        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("name", "12345678900");
        reservationRequest.put("date", "2025-12-12");
        reservationRequest.put("timeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("올바르지 않은 날짜 형식으로 입력시 예외처리")
    @Test
    void invalidDateFormat() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));

        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("name", "1234567890");
        reservationRequest.put("date", "2025-aa-bb");
        reservationRequest.put("timeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("올바르지 않은 예약자명 형식으로 입력시 예외처리")
    @Test
    void invalidTimeIdFormat() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));

        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("name", "12345678900");
        reservationRequest.put("date", "2030-12-12");
        reservationRequest.put("timeId", "a");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("지나간 시점에 대한 예약시 예외처리")
    @Test
    void pastTimeSlotReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));

        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("name", "1234567890");
        reservationRequest.put("date", "1999-12-12");
        reservationRequest.put("timeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("동일한 시간에 중복 예약시 예외 처리")
    @Test
    void duplicateReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES(?, ?, ?)",
                "rush", LocalDate.of(2030, 12, 12), 1);

        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("name", "1234567890");
        reservationRequest.put("date", "2030-12-12");
        reservationRequest.put("timeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
