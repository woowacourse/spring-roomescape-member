package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.ReservationController;
import roomescape.controller.TimeController;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {
    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationController reservationController;

    @Autowired
    private TimeController timeController;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    //TODO : 테스트 메서드 컨밴션 변경
    @DisplayName("admin 페이지 URL 요청이 올바르게 연결된다.")
    @Test
    void 일단계() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("reservation 페이지 URL 요청과 조회 요청이 올바르게 연결된다.")
    @Test
    void 이단계() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1)); // 아직 생성 요청이 없으니 Controller에서 임의로 넣어준 Reservation 갯수 만큼 검증하거나 0개임을 확인하세요.
    }

    @DisplayName("h2 데이터베이스가 연결되었는지 확인한다.")
    @Test
    void 사단계() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 칠단계() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:10");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));

        RestAssured.given().log().all()
                .when().delete("/times/2")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("h2 데이터베이스를 활용하여 reservation 페이지에 새로운 예약 정보를 추가, 조회, 삭제할 수 있다.")
    @Test
    void 팔단계() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2999-12-31");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("등록되지 않은 시간으로 예약하는 경우 400 오류를 반환한다.")
    @Test
    void given_when_saveNotExistTimeId_then_statusCodeIsBadRequest() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2099-01-01");
        reservation.put("timeId", 500);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("비어있는 이름으로 예약하는 경우 400 오류를 반환한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void given_when_saveInvalidName_then_statusCodeIsBadRequest(String invalidName) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", invalidName);
        reservation.put("date", "2099-01-01");
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] 이름은 비워둘 수 없습니다."));
    }

    @DisplayName("부적절한 날짜로 예약하는 경우 400 오류를 반환한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", "", "2011-02-09"})
    void given_when_saveInvalidDate_then_statusCodeIsBadRequest(String invalidDate) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", invalidDate);
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] 올바르지 않은 예약 날짜입니다."));
    }

    @DisplayName("부적절한 시간으로 예약하는 경우 400 오류를 반환한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", "", "0", "-1"})
    void given_when_saveInvalidTimeId_then_statusCodeIsBadRequest(String invalidTimeId) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2999-12-31");
        reservation.put("timeId", invalidTimeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] 올바르지 않은 예약 시간입니다."));
    }

    @DisplayName("지나간 날짜와 시간으로 예약할 경우 400 오류를 반환한다.")
    @Test
    void given_when_saveWithPastReservation_then_statusCodeIsBadRequest() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", LocalDate.now().toString());
        reservation.put("timeId", 1); // 10:00
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] 지나간 날짜와 시간으로 예약할 수 없습니다"));
    }

    @DisplayName("이미 예약이 된 시간을 등록하려 하면 400 오류를 반환한다.")
    @Test
    void given_when_saveDuplicatedReservation_then_statusCodeIsBadRequest() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "포케");
        reservation.put("date", "2099-04-30");
        reservation.put("timeId", 1); // 10:00
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] 예약이 찼어요 ㅜㅜ 죄송해요~~"));
    }

    @DisplayName("이미 등록된 시간을 등록하면 400 오류를 반환한다.")
    @Test
    void given_when_saveDuplicatedTime_then_statusCodeIsBadRequest() {
        Map<String, Object> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] 이미 등록된 시간입니다"));
    }

    @DisplayName("비어있는 시간으로 등록하는 경우 400 오류를 반환한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void given_when_saveInvalidTime_then_statusCodeIsBadRequest(String invalidTime) {
        Map<String, Object> time = new HashMap<>();
        time.put("startAt", invalidTime);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] 잘못된 시간입니다"));
    }

    @DisplayName("부적절한 양식으로 시간을 등록하는 경우 개발자가 정의한 문구로 400 오류를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"99:99", "10:00:01"})
    void given_when_saveInvalidTime_then_statusCodeIsBadRequestWithCustomMessage(String invalidTime) {
        Map<String, Object> time = new HashMap<>();
        time.put("startAt", invalidTime);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] 적절하지 않은 입력값 입니다"));
    }

    @DisplayName("삭제하고자 하는 시간에 예약이 등록되어 있으면 400 오류를 반환한다.")
    @Test
    void given_when_deleteTimeIdRegisteredReservation_then_statusCodeIsBadRequest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] 예약이 등록된 시간은 제거할 수 없습니다"));
    }

    @DisplayName("theme 페이지 URL 요청이 올바르게 연결된다.")
    @Test
    void given_when_GetThemePage_then_statusCodeIsOkay() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("theme 목록 조회 요청이 올바르게 동작한다.")
    @Test
    void given_when_GetThemes_then_statusCodeIsOkay() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("theme 등록 및 삭제 요청이 올바르게 동작한다.")
    @Test
    void given_themeRequest_when_postAndDeleteTheme_then_statusCodeIsOkay() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "우테코 레벨 1 탈출");
        params.put("description", "우테코 레벨 1 탈출하는 내용");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));

        RestAssured.given().log().all()
                .when().delete("/themes/2")
                .then().log().all()
                .statusCode(204);
    }
}
