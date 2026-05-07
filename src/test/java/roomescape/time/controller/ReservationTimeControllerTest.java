package roomescape.time.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    private static String startAt1 = "10:00:00";
    private static String startAt2 = "11:00:00";
    private static String themeName = "테마1";
    private static String name = "브라운";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자는 특정 날짜와 테마의 예약 가능한 시간을 조회한다.")
    void readAvailableTimes() {
        String date = LocalDate.now().plusDays(1).toString();

        Integer timeId = createTime(startAt1);
        createDate(date);
        Integer themeId = createTheme(themeName);

        RestAssured.given().log().all()
                .queryParam("date", date)
                .queryParam("themeId", themeId)
                .when().get("/member/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", is(timeId))
                .body("[0].startAt", is(startAt1));
    }

    @Test
    @DisplayName("이미 예약된 시간은 예약 가능한 시간 목록에서 제외된다.")
    void readAvailableTimesExcludeReservedTime() {
        String date = LocalDate.now().plusDays(1).toString();

        Integer reservedTimeId = createTime(startAt1);
        Integer availableTimeId = createTime(startAt2);
        Integer dateId = createDate(date);
        Integer themeId = createTheme(themeName);

        createReservation(name, dateId, reservedTimeId, themeId);

        RestAssured.given().log().all()
                .queryParam("date", date)
                .queryParam("themeId", themeId)
                .when().get("/member/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", is(availableTimeId))
                .body("[0].startAt", is(startAt2));
    }

    @Test
    @DisplayName("예약 시간이 없으면 빈 목록을 반환한다.")
    void readAvailableTimesEmpty() {
        String date = LocalDate.now().plusDays(1).toString();

        createDate(date);
        Integer themeId = createTheme(themeName);

        RestAssured.given().log().all()
                .queryParam("date", date)
                .queryParam("themeId", themeId)
                .when().get("/member/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
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

    private Integer createTheme(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("description", name + " 설명");
        params.put("thumbnailUrl", name + " 썸네일");

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");
    }

    private void createReservation(String name, Integer dateId, Integer timeId, Integer themeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
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

}
