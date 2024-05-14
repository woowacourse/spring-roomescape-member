package roomescape.controller.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static roomescape.util.Fixture.MEMBER_EMAIL;
import static roomescape.util.Fixture.MEMBER_PASSWORD;
import static roomescape.util.Fixture.PAST_TIME;
import static roomescape.util.Fixture.TODAY;
import static roomescape.util.Fixture.TOMORROW;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class ReservationControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("로그인한 회원이 예약을 생성한다.")
    void createReservationByLoginMember() {
        final String memberToken = getMemberToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("예약 생성 시, 쿠키에 로그인 정보가 없으면 예외가 발생한다.")
    void createReservationByLoginMemberWithoutLogin() {
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(401);
    }

    @ParameterizedTest
    @DisplayName("예약 생성 시, date이 null이거나 형식이 올바르지 않으면 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "abc"})
    void validateReservationCreateWithDateFormat(final String date) {
        final String memberToken = getMemberToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, date가 이미 지난 날짜면 예외가 발생한다.")
    void validateReservationCreateWithPastDate() {
        final String memberToken = getMemberToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", "2020-10-10");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, date는 오늘이고 time은 이미 지난 시간이면 예외가 발생한다.")
    void validateReservationCreateWithTodayPastTime() {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", PAST_TIME);

        final long savedTimeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201).extract()
                .jsonPath().getLong("id");

        final String memberToken = getMemberToken();
        final Map<String, Object> reservationParams = new HashMap<>();
        reservationParams.put("date", TODAY);
        reservationParams.put("timeId", savedTimeId);
        reservationParams.put("themeId", 1);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, timeId가 null이면 예외가 발생한다.")
    void validateReservationCreateWithNullTimeId() {
        final String memberToken = getMemberToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", null);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, timeId 값으로 찾을 수 있는 시간이 없으면 예외가 발생한다.")
    void validateReservationCreateWithTimeIdNotFound() {
        final String memberToken = getMemberToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", -1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("예약 생성 시, themeId가 null이면 예외가 발생한다.")
    void validateReservationCreateWithNullThemeId() {
        final String memberToken = getMemberToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", 1);
        params.put("themeId", null);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 생성 시, themeId 값으로 찾을 수 있는 테마가 없으면 예외가 발생한다.")
    void validateReservationCreateWithThemeIdNotFound() {
        final String memberToken = getMemberToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", 1);
        params.put("themeId", -1);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("예약 생성 시, 해당 날짜 시간 테마에 예약 내역이 있으면 예외가 발생한다.")
    void validateReservationCreateWithDuplicatedDateAndTimeAndTheme() {
        final String memberToken = getMemberToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
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
    @DisplayName("사용자 id로 예약 목록을 조회한다.")
    void findReservationsWithMemberId() {
        RestAssured.given().log().all()
                .when().get("/reservations?member-id=2")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    @DisplayName("테마 id로 예약 목록을 조회한다.")
    void findReservationsWithThemeId() {
        RestAssured.given().log().all()
                .when().get("/reservations?theme-id=3")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("날짜 구간으로 예약 목록을 조회한다.")
    void findReservationsWithDate() {
        RestAssured.given().log().all()
                .when().get("/reservations?date-from=2024-05-06&date-to=2024-05-07")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("사용자 id, 테마 id, 날짜 구간으로 예약 목록을 조회한다.")
    void findReservationsWithConditions() {
        RestAssured.given().log().all()
                .when().get("/reservations?member-id=1&theme-id=2&date-from=2024-05-05")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 내역을 삭제할 수 있다")
    void deleteReservation() {
        final String memberToken = getMemberToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", 1);
        params.put("themeId", 1);

        final long savedReservationId = RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        final int beforeSize = countReservation();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/reservations/" + savedReservationId)
                .then().log().all()
                .statusCode(204);

        final int afterSize = countReservation();

        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

    private String getMemberToken() {
        final Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", MEMBER_EMAIL);
        loginParams.put("password", MEMBER_PASSWORD);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(loginParams)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];
    }

    private int countReservation() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM reservation", Integer.class);
    }
}
