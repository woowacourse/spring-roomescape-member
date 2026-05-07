package roomescape.reservation.controller;

import static org.hamcrest.Matchers.is;

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
class ReservationAdminControllerTest {

    private final String reservationName = "브라운";

    private final String date = LocalDate.of(2099, 1, 1).toString();
    private final String startAt = "10:00";

    private final String themeName = "테마1";
    private final String themeDescription = "테마1 설명";
    private final String thumbnailUrl = "테마1 썸네일";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자는 전체 예약 목록을 조회한다.")
    void getReservations() {
        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("관리자는 예약을 생성한다.")
    void createReservation() {
        Integer dateId = createDate(date);
        Integer timeId = createTime(startAt);
        Integer themeId = createTheme(themeName);

        createReservationByAdmin(reservationName, dateId, timeId, themeId);

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("관리자는 예약을 취소한다.")
    void cancelReservation() {
        Integer dateId = createDate(date);
        Integer timeId = createTime(startAt);
        Integer themeId = createTheme(themeName);

        Integer reservationId = createReservationByAdmin(reservationName, dateId, timeId, themeId);

        RestAssured.given().log().all()
                .when().patch("/admin/reservations/" + reservationId)
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
    void createReservationWithoutName() {
        Integer dateId = createDate(date);
        Integer timeId = createTime(startAt);
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
    void createReservationWithoutDateId() {
        Integer timeId = createTime(startAt);
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
                .statusCode(400);
    }

    @Test
    @DisplayName("timeId가 없으면 예약 생성에 실패한다.")
    void createReservationWithoutTimeId() {
        Integer dateId = createDate(date);
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
                .statusCode(400);
    }

    @Test
    @DisplayName("themeId가 없으면 예약 생성에 실패한다.")
    void createReservationWithoutThemeId() {
        Integer dateId = createDate(date);
        Integer timeId = createTime(startAt);

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
                .statusCode(400);
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

    private Integer createDate(String date) {
        Map<String, String> params = new HashMap<>();
        params.put("date", date);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/dates")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");
    }

    private Integer createTime(String startAt) {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", startAt);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");
    }

    private Integer createTheme(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("description", themeDescription);
        params.put("thumbnailUrl", thumbnailUrl);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");
    }

}
