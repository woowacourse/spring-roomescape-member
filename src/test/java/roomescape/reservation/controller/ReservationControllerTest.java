package roomescape.reservation.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.date.fixture.ReservationDateApiFixture.createReservationDate;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationControllerTest {

    private final String reservationName = "브라운";
    private final String otherReservationName = "코니";

    private final String date = LocalDate.of(2099, 1, 1).toString();
    private final String startAt = "10:00";
    private final String otherStartAt = "11:00";

    private final String themeName = "테마1";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자는 예약을 생성한다.")
    void create_reservation() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);
        createReservation(reservationName, dateId, timeId, themeId);

        RestAssured.given().log().all()
                .when().get("/member/reservations/" + reservationName)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("사용자는 자신의 이름으로 예약 목록을 조회한다.")
    void get_my_reservations() {
        Integer dateId = createReservationDate(date);
        Integer themeId = createTheme(themeName);

        Integer timeId = createReservationTime(startAt);
        Integer otherTimeId = createReservationTime(otherStartAt);

        createReservation(reservationName, dateId, timeId, themeId);
        createReservation(otherReservationName, dateId, otherTimeId, themeId);

        RestAssured.given().log().all()
                .when().get("/member/reservations/" + reservationName)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().get("/member/reservations/" + otherReservationName)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약이 없는 이름으로 조회하면 빈 목록을 반환한다.")
    void get_my_reservations_empty() {
        RestAssured.given().log().all()
                .when().get("/member/reservations/" + reservationName)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
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
                .when().post("/member/reservations")
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
                .when().post("/member/reservations")
                .then().log().all()
                .statusCode(400);
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
                .when().post("/member/reservations")
                .then().log().all()
                .statusCode(400);
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
                .when().post("/member/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약된 날짜/시간/테마를 중복 예약하면 예외가 발생한다.")
    void reserved_duplicated() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        createReservation(reservationName, dateId, timeId, themeId);

        Map<String, Object> params = new HashMap<>();
        params.put("name", reservationName);
        params.put("dateId", dateId);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/member/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("해당 날짜/시간/테마는 이미 예약되었습니다."));
    }

    @Test
    @DisplayName("취소된 예약을 동일한 사람이 새롭게 예약할 수 있다.")
    void reserved_when_canceled_same_name() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);
        cancelReservation(reservationId, reservationName);

        Map<String, Object> params = new HashMap<>();
        params.put("name", reservationName);
        params.put("dateId", dateId);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/member/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("취소된 예약을 동일한 사람이 새롭게 예약할 수 있다.")
    void reserved_when_canceled_another_name() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);
        cancelReservation(reservationId, reservationName);

        String anotherName = "다른사람";
        Map<String, Object> params = new HashMap<>();
        params.put("name", anotherName);
        params.put("dateId", dateId);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/member/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("사용자는 자신의 예약을 취소한다.")
    void cancel() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);

        Map<String, String> params = new HashMap<>();
        params.put("name", reservationName);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/member/reservations/" + reservationId + "/cancel")
                .then().log().all()
                .statusCode(200)
                .body("status", is("CANCELED"));
    }

    @Test
    @DisplayName("본인의 예약이 아닌데 취소하면 예외가 발생한다.")
    void cancel_not_owner() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);
        String anotherName = "다른사람";

        Map<String, String> params = new HashMap<>();
        params.put("name", anotherName);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/member/reservations/" + reservationId + "/cancel")
                .then().log().all()
                .statusCode(400)
                .body("message", is("본인의 예약만 취소할 수 있습니다."));
    }

    @Test
    @DisplayName("이미 취소된 예약을 취소하면 예외가 발생한다.")
    void cancel_already_canceled() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);
        cancelReservation(reservationId, reservationName);

        Map<String, String> params = new HashMap<>();
        params.put("name", reservationName);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/member/reservations/" + reservationId + "/cancel")
                .then().log().all()
                .statusCode(400)
                .body("message", is("이미 취소된 예약입니다."));
    }

    @Test
    @DisplayName("이미 지난 예약을 취소하면 예외가 발생한다.")
    @Sql(
            scripts = "classpath:past-reservation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void cancel_not_past() {
        String sqlRequsterName = "송송";
        Long sqlSavedId = 1L;

        Map<String, String> params = new HashMap<>();
        params.put("name", sqlRequsterName);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/member/reservations/" + sqlSavedId + "/cancel")
                .then().log().all()
                .statusCode(400)
                .body("message", is("이미 지난 예약입니다."));
    }

    @Test
    @DisplayName("예약 가능한 날짜로 변경할 수 있다.")
    void changeSchedule() {
        String futureDate = LocalDate.now().plusDays(1).toString();
        String futureTime = LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS).toString();
        Integer dateId = createReservationDate(date);
        Integer changedDateId = createReservationDate(futureDate);
        Integer timeId = createReservationTime(startAt);
        Integer changedTimeId = createReservationTime(futureTime);
        Integer themeId = createTheme(themeName);
        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);

        Map<String, Object> params = new HashMap<>();
        params.put("name", reservationName);
        params.put("dateId", changedDateId);
        params.put("timeId", changedTimeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/member/reservations/" + reservationId + "/schedule?name=" + reservationName)
                .then().log().all()
                .statusCode(200)
                .body("date", is(futureDate))
                .body("time", is(futureTime));
    }

    @Test
    @DisplayName("본인의 예약이 아닌데 변경을 시도하면 예외가 발생한다.")
    void changeSchedule_not_owner() {
        Integer dateId = createReservationDate(date);
        Integer changedDateId = createReservationDate(LocalDate.now().plusDays(1).toString());
        Integer timeId = createReservationTime(startAt);
        Integer changedTimeId = createReservationTime(LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS).toString());
        Integer themeId = createTheme(themeName);
        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);

        String notOwnerName = "다른사람";
        Map<String, Object> params = new HashMap<>();
        params.put("dateId", changedDateId);
        params.put("timeId", changedTimeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/member/reservations/" + reservationId + "/schedule?name=" + notOwnerName)
                .then().log().all()
                .statusCode(400)
                .body("message", is("본인의 예약만 취소할 수 있습니다."));
    }

    @Test
    @DisplayName("이미 취소된 예약을 변경하면 예외가 발생한다.")
    void changeSchedule_already_canceled() {
        Integer dateId = createReservationDate(date);
        Integer changedDateId = createReservationDate(LocalDate.now().plusDays(1).toString());
        Integer timeId = createReservationTime(startAt);
        Integer changedTimeId = createReservationTime(LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS).toString());
        Integer themeId = createTheme(themeName);
        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);
        cancelReservation(reservationId, reservationName);

        Map<String, Object> params = new HashMap<>();
        params.put("name", reservationName);
        params.put("dateId", changedDateId);
        params.put("timeId", changedTimeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/member/reservations/" + reservationId + "/schedule?name=" + reservationName)
                .then().log().all()
                .statusCode(400)
                .body("message", is("이미 취소된 예약입니다."));
    }

    @Test
    @DisplayName("이미 지난 예약을 변경하면 예외가 발생한다.")
    @Sql(
            scripts = "classpath:past-reservation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void changeSchedule_past() {
        Integer changedDateId = createReservationDate(LocalDate.now().plusDays(1).toString());
        Integer changedTimeId = createReservationTime(LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS).toString());

        String sqlRequsterName = "송송";
        Long sqlSavedId = 1L;

        Map<String, Object> params = new HashMap<>();
        params.put("name", sqlRequsterName);
        params.put("dateId", changedDateId);
        params.put("timeId", changedTimeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/member/reservations/" + sqlSavedId + "/schedule?name=" + sqlRequsterName)
                .then().log().all()
                .statusCode(400)
                .body("message", is("이미 지난 예약입니다."));
    }

    @Test
    @DisplayName("지난 날짜/시간으로 예약을 변경하면 예외가 발생한다.")
    @Sql(
            scripts = "classpath:past-reservation-date.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void changeSchedule_new_datetime_is_past() {
        Integer dateId = createReservationDate(date);
        Integer pastDateId = 1;
        Integer timeId = createReservationTime(startAt);
        Integer changedTimeId = createReservationTime(LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS).toString());
        Integer themeId = createTheme(themeName);
        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);

        Map<String, Object> params = new HashMap<>();
        params.put("name", reservationName);
        params.put("dateId", pastDateId);
        params.put("timeId", changedTimeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/member/reservations/" + reservationId + "/schedule?name=" + reservationName)
                .then().log().all()
                .statusCode(400)
                .body("message", is("이미 지난 날짜/시간을 예약할 수 없습니다."));
    }

}
