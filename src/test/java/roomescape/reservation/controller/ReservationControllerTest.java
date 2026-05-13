package roomescape.reservation.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.date.fixture.ReservationDateApiFixture.createReservationDate;
import static roomescape.reservation.fixture.ReservationApiFixture.createReservation;
import static roomescape.theme.fixture.ThemeApiFixture.createTheme;
import static roomescape.time.fixture.ReservationTimeApiFixture.createReservationTime;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.time.LocalDate;
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
    @DisplayName("사용자는 자신의 예약을 취소한다.")
    void cancel_reservation() {
        Integer dateId = createReservationDate(date);
        Integer timeId = createReservationTime(startAt);
        Integer themeId = createTheme(themeName);

        Integer reservationId = createReservation(reservationName, dateId, timeId, themeId);

        RestAssured.given().log().all()
                .when().patch("/member/reservations/" + reservationId + "/cancel")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/member/reservations/" + reservationName)
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

}
