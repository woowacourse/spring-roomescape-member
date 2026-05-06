package roomescape;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-test.sql", "classpath:reset-test.sql", "classpath:data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomescapeApplicationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 사용자는_조건에_맞는_새로운_예약을_생성할_수_있다() {
        Map<String, Object> request = new HashMap<>();
        request.put("themeId", 1);
        request.put("name", "캐모");
        request.put("date", "2026-05-07");
        request.put("timeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 이미_예약된_시간에_중복_예약을_시도하면_400_예외가_발생한다() {
        Map<String, Object> request = new HashMap<>();
        request.put("themeId", 1);
        request.put("name", "캐모");
        request.put("date", "2026-05-01");
        request.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 예약자의_이름_JSON을_전송하여_예약을_삭제할_수_있다() {
        Map<String, String> deleteRequest = Map.of("name", "User1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(deleteRequest)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 관리자는_새로운_테마를_생성할_수_있다() {
        Map<String, String> request = Map.of(
                "name", "새로운 테마",
                "description", "설명입니다.",
                "thumbnail", "https://example.com/new.png"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue());
    }

    @Test
    void 사용자는_등록된_테마_목록을_조회할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(4));
    }

    @Test
    void 관리자는_특정_테마를_삭제할_수_있다() {
        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 새로운_예약_시간을_생성할_수_있다() {
        Map<String, String> request = Map.of("startAt", "15:00:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("startAt", is("15:00:00"));
    }

    @Test
    void 예약이_없는_시간은_정상적으로_삭제할_수_있다() {
        long timeId = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", "23:00:00"))
                .post("/admin/times")
                .jsonPath().getLong("id");

        RestAssured.given().log().all()
                .when().delete("/admin/times/" + timeId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 이미_예약이_존재하는_시간을_삭제하려_하면_409_Conflict가_발생한다() {
        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value());
    }


    @Test
    void 특정_날짜와_테마의_스케줄_조회_시_예약_상태_isAvailable가_정확히_반환된다() {
        RestAssured.given().log().all()
                .queryParam("date", "2026-05-01")
                .when().get("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("themeId", is(1))
                .body("date", is("2026-05-01"))
                .body("schedules.find { it.timeId == 1 }.isAvailable", is(false))
                .body("schedules.find { it.timeId == 2 }.isAvailable", is(true));
    }

    @Test
    void 파라미터_없이_랭킹을_조회하면_최근_7일_기준_상위_10개_기본값의_테마가_반환된다() {
        RestAssured.given().log().all()
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(4))
                .body("[0].id", is(1))
                .body("[1].id", is(2))
                .body("[2].id", is(3))
                .body("[3].id", is(4));
    }

    @Test
    void limit_파라미터를_2로_지정하면_상위_2개의_테마만_반환된다() {
        RestAssured.given().log().all()
                .queryParam("limit", 2)
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[1].id", is(2));
    }

    @Test
    void 특정_날짜_startDate_endDate를_지정하여_랭킹을_조회할_수_있다() {
        RestAssured.given().log().all()
                .queryParam("startDate", "2026-05-05")
                .queryParam("endDate", "2026-05-06")
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", is(1));
    }

    private long createTheme(String name) {
        Map<String, String> request = Map.of(
                "name", name,
                "description", "설명입니다.",
                "thumbnail", "https://example.com/thumb.png"
        );

        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/admin/themes");

        if (response.statusCode() != HttpStatus.CREATED.value()) {
            throw new AssertionError("Create theme failed: status=" + response.statusCode()
                    + ", body=" + response.asString());
        }

        return response.jsonPath().getLong("id");
    }

    private long createTime(String startAt) {
        Map<String, String> request = Map.of("startAt", startAt);

        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/admin/times");

        if (response.statusCode() != HttpStatus.CREATED.value()) {
            throw new AssertionError("Create time failed: status=" + response.statusCode()
                    + ", body=" + response.asString());
        }

        return response.jsonPath().getLong("id");
    }

    private long createReservation(long themeId, long timeId, String date, String name) {
        Map<String, Object> request = new HashMap<>();
        request.put("themeId", themeId);
        request.put("name", name);
        request.put("date", date);
        request.put("timeId", timeId);

        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/reservations");

        if (response.statusCode() != HttpStatus.CREATED.value()) {
            throw new AssertionError("Create reservation failed: status=" + response.statusCode()
                    + ", body=" + response.asString());
        }

        return response.jsonPath().getLong("id");
    }

    private long[] seedRankedThemes() {
        LocalDate today = LocalDate.now();
        String date1 = today.minusDays(1).toString();
        String date2 = today.minusDays(2).toString();
        String date3 = today.minusDays(3).toString();
        String date4 = today.minusDays(4).toString();

        long themeAId = createTheme("Theme A");
        long themeBId = createTheme("Theme B");
        long themeCId = createTheme("Theme C");
        long themeDId = createTheme("Theme D");

        long time1 = createTime("10:00:00");
        long time2 = createTime("11:00:00");
        long time3 = createTime("12:00:00");
        long time4 = createTime("13:00:00");

        createReservation(themeAId, time1, date1, "User1");
        createReservation(themeAId, time2, date2, "User2");
        createReservation(themeAId, time3, date3, "User3");
        createReservation(themeAId, time4, date4, "User4");

        createReservation(themeBId, time1, date1, "User5");
        createReservation(themeBId, time2, date2, "User6");
        createReservation(themeBId, time3, date3, "User7");

        createReservation(themeCId, time1, date1, "User8");
        createReservation(themeCId, time2, date2, "User9");

        createReservation(themeDId, time1, date1, "User10");

        return new long[]{themeAId, themeBId, themeCId, themeDId};
    }
}
