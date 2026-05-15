package roomescape.reservation.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.date.fixture.ReservationDateApiFixture.createReservationDate;
import static roomescape.reservation.exception.ReservaitonErrorInformation.*;
import static roomescape.reservation.fixture.ReservationApiFixture.cancelReservation;
import static roomescape.reservation.fixture.ReservationApiFixture.createReservation;
import static roomescape.theme.fixture.ThemeApiFixture.createTheme;
import static roomescape.time.fixture.ReservationTimeApiFixture.createReservationTime;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationAdminControllerTest {

    private final String reservationName = "브라운";

    private final String date = LocalDate.of(2099, 1, 1).toString();
    private final String startAt = "11:00";

    private final String themeName = "테마1";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자는 전체 예약 목록을 조회한다.")
    void get_reservations() {
        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("관리자는 예약을 생성한다.")
    void create_reservation() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        createReservationByAdmin(reservationName, dateId, timeId, themeId);

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("관리자는 예약을 취소할 수 있다.")
    void cancelByManager_reservation() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        Integer reservationId = createReservationByAdmin(reservationName, dateId, timeId, themeId);

        RestAssured.given().log().all()
                .when().patch("/admin/reservations/" + reservationId + "/cancel")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("name이 없으면 예약 생성에 실패한다.")
    void create_reservation_without_name() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        params.put("dateId", dateId);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("dateId가 없으면 예약 생성에 실패한다.")
    void create_reservation_without_date_id() {
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        Map<String, Object> params = new HashMap<>();
        params.put("name", reservationName);
        params.put("dateId", null);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("요청 값 검증에 실패했습니다."));
    }

    @Test
    @DisplayName("timeId가 없으면 예약 생성에 실패한다.")
    void create_reservation_without_time_id() {
        Integer dateId = createReservationDate(date);
        Integer themeId = createTheme(themeName);

        Map<String, Object> params = new HashMap<>();
        params.put("name", reservationName);
        params.put("dateId", dateId);
        params.put("timeId", null);
        params.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("요청 값 검증에 실패했습니다."));
    }

    @Test
    @DisplayName("themeId가 없으면 예약 생성에 실패한다.")
    void create_reservation_without_theme_id() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);

        Map<String, Object> params = new HashMap<>();
        params.put("name", reservationName);
        params.put("dateId", dateId);
        params.put("timeId", timeId);
        params.put("themeId", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("요청 값 검증에 실패했습니다."));
    }

    private Integer createReservationByAdmin(String name, Integer dateId, Integer timeId, Integer themeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("dateId", dateId);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("관리자는 예약자 확인 없이, 예약 날짜/시간을 변경할 수 있다.")
    void updateSchedule() {
        String futureDate = LocalDate.now().plusDays(1).toString();
        String futureTime = LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS).toString();
        Integer dateId = createReservationDate(date);
        Integer changedDateId = createReservationDate(futureDate);
        Integer timeId = createReservationTime(startAt);
        Integer changedTimeId = createReservationTime(futureTime);
        Integer themeId = createTheme(themeName);
        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);

        Map<String, Object> params = new HashMap<>();
        params.put("dateId", changedDateId);
        params.put("timeId", changedTimeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/admin/reservations/" + reservationId + "/schedule")
                .then().log().all()
                .statusCode(200)
                .body("date", is(futureDate))
                .body("time", is(futureTime));
    }

    @Test
    @DisplayName("이미 취소된 예약을 변경하면 예외가 발생한다.")
    void updateScheduleByManager_already_canceled() {
        Integer dateId = createReservationDate(date);
        Integer changedDateId = createReservationDate(LocalDate.now().plusDays(1).toString());
        Integer timeId = createReservationTime(startAt);
        Integer changedTimeId = createReservationTime(LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS).toString());
        Integer themeId = createTheme(themeName);
        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);
        cancelReservation(reservationId, reservationName);

        Map<String, Object> params = new HashMap<>();
        params.put("dateId", changedDateId);
        params.put("timeId", changedTimeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/admin/reservations/" + reservationId + "/schedule")
                .then().log().all()
                .statusCode(RESERVATION_ALREADY_CANCELED.getHttpStatus().value())
                .body("message", is(RESERVATION_ALREADY_CANCELED.getMessage()));
    }

    @Test
    @DisplayName("관리자가 이미 존재하는 날짜/시간으로 예약을 변경하면 예외가 발생한다.")
    void updateScheduleByManager_duplicated() {
        Integer dateId = createReservationDate(date);
        Integer alreadyReservedDateId = createReservationDate(LocalDate.now().plusDays(1).toString());
        Integer timeId = createReservationTime(startAt);
        Integer alreadyReservedTimeId = createReservationTime(LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS).toString());
        Integer themeId = createTheme(themeName);
        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);
        createReservation(reservationName, alreadyReservedDateId, alreadyReservedTimeId, themeId);

        Map<String, Object> params = new HashMap<>();
        params.put("dateId", alreadyReservedDateId);
        params.put("timeId", alreadyReservedTimeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/admin/reservations/" + reservationId + "/schedule")
                .then().log().all()
                .statusCode(RESERVATION_ALREADY_BOOKED.getHttpStatus().value())
                .body("message", is(RESERVATION_ALREADY_BOOKED.getMessage()));
    }

    @Test
    @DisplayName("관리자가 예약을 과거의 날짜/시간으로 변경하면 예외가 발생한다.")
    @Sql(
            scripts = "classpath:past-reservation-date.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void updateScheduleByManager_pastDateTime() {
        Integer dateId = createReservationDate(date);
        Integer pastSqlDateId = 1;
        Integer timeId = createReservationTime(startAt);
        Integer pastTimeId = createReservationTime("00:01");
        Integer themeId = createTheme(themeName);
        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);

        Map<String, Object> params = new HashMap<>();
        params.put("dateId", pastSqlDateId);
        params.put("timeId", pastTimeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/admin/reservations/" + reservationId + "/schedule")
                .then().log().all()
                .statusCode(RESERVATION_NEW_SCHEDULE_PAST_NOT_ALLOWED.getHttpStatus().value())
                .body("message", is(RESERVATION_NEW_SCHEDULE_PAST_NOT_ALLOWED.getMessage()));
    }

}
