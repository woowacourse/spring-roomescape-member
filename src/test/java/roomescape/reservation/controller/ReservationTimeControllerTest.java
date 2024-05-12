package roomescape.reservation.controller;

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
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.reservation.ReservationController;
import roomescape.controller.reservation.ReservationThemeController;
import roomescape.controller.reservation.ReservationTimeController;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.time.TimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql"})
public class ReservationTimeControllerTest {

    @Autowired
    private ReservationController reservationController;

    @Autowired
    private ReservationTimeController timeController;

    @Autowired
    private ReservationThemeController themeController;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("시간 조회 요청 시 200으로 응답한다.")
    @Test
    void times() {
        // given
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        timeController.createTime(new TimeRequest(LocalTime.parse("11:00")));

        // when & then
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("정상적인 시간 추가 요청 시 201으로 응답한다.")
    @Test
    void insert() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "10:00");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .body("startAt", is("10:00"));
    }

    @DisplayName("시간 삭제 요청 시 204로 응답한다.")
    @Test
    void delete() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("예약이 존재하는 시간은 삭제할 수 없다.")
    @Test
    void invalidDeleteTime() {
        // given
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));
        reservationController.createReservation(new ReservationRequest("user", LocalDate.parse("2025-01-01"), 1L, 1L));

        // when & then
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400)
                .body("message", is("예약이 존재하는 시간은 삭제할 수 없습니다."));
    }

    @DisplayName("예약 가능한 시간을 조회한다.")
    @Test
    void getTimeByDateAndTheme() {
        // given
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        timeController.createTime(new TimeRequest(LocalTime.parse("11:00")));
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));
        reservationController.createReservation(new ReservationRequest("user", LocalDate.parse("2025-01-01"), 1L, 1L));

        // when & then
        RestAssured.given().log().all()
                .when().get("/times/2025-01-01/1")
                .then().log().all()
                .statusCode(200)
                .body("[0].booked", is(true))
                .body("[1].booked", is(false));
    }
}
