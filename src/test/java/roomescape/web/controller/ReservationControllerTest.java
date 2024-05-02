package roomescape.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class ReservationControllerTest {
    private static final String TOMORROW_DATE = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("예약 생성 시, name이 null이면 예외가 발생한다.")
    void validateReservationCreateWithNullName() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("date", TOMORROW_DATE);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("예약 생성 시, name이 빈값이면 예외가 발생한다.")
    void validateReservationCreateWithEmptyName(final String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", TOMORROW_DATE);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, date가 null이면 예외가 발생한다.")
    void validateReservationCreateWithNullDate() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", null);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "abc"})
    @DisplayName("예약 생성 시, date의 형식이 올바르지 않으면 예외가 발생한다.")
    void validateReservationCreateWithDateFormat(final String date) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", date);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, date가 이미 지난 날짜면 예외가 발생한다.")
    void validateReservationCreateWithPastDate() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2020-10-10");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, date는 오늘이고 time은 이미 지난 시간이면 예외가 발생한다.")
    void validateReservationCreateWithTodayPastTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> reservationParams = new HashMap<>();
        reservationParams.put("name", "브라운");
        reservationParams.put("date", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        reservationParams.put("timeId", findLastIdOfReservationTime());
        reservationParams.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, timeId가 null이면 예외가 발생한다.")
    void validateReservationCreateWithNullTimeId() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", TOMORROW_DATE);
        params.put("timeId", null);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, timeId 값으로 찾을 수 있는 시간이 없으면 예외가 발생한다.")
    void validateReservationCreateWithTimeIdNotFound() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", TOMORROW_DATE);
        params.put("timeId", findLastIdOfReservationTime() + 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, themeId가 null이면 예외가 발생한다.")
    void validateReservationCreateWithNullThemeId() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", TOMORROW_DATE);
        params.put("timeId", 1);
        params.put("themeId", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, themeId 값으로 찾을 수 있는 테마가 없으면 예외가 발생한다.")
    void validateReservationCreateWithThemeIdNotFound() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", TOMORROW_DATE);
        params.put("timeId", 1);
        params.put("themeId", findLastIdOfTheme() + 1);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, 해당 날짜 시간 테마에 예약 내역이 있으면 예외가 발생한다.")
    void validateReservationCreateWithDuplicatedDateAndTimeAndTheme() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", TOMORROW_DATE);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(201);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(400);
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    void findAllReservations() {
        RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(countReservation()));
    }

    @Test
    @DisplayName("예약 내역을 삭제할 수 있다")
    void deleteReservation() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", TOMORROW_DATE);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(201);

        int beforeSize = countReservation();

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .when().delete("/reservations/" + findLastIdOfReservation())
            .then().log().all()
            .statusCode(204);

        int afterSize = countReservation();

        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

    private int findLastIdOfReservationTime() {
        return jdbcTemplate.queryForObject("SELECT max(id) FROM reservation_time", Integer.class);
    }

    private int findLastIdOfTheme() {
        return jdbcTemplate.queryForObject("SELECT max(id) FROM theme", Integer.class);
    }

    private int findLastIdOfReservation() {
        return jdbcTemplate.queryForObject("SELECT max(id) FROM reservation", Integer.class);
    }

    private int countReservation() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM reservation", Integer.class);
    }
}
