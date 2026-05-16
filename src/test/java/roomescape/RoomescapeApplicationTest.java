package roomescape;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-test.sql", "classpath:reset-test.sql", "classpath:data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
        request.put("themeId", themeId("Theme A"));
        request.put("name", "캐모");
        request.put("date", LocalDate.now().plusDays(1).toString());
        request.put("timeId", timeId("11:00:00"));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 지나간_날짜에_예약을_시도하면_422_예외가_발생한다() {
        Map<String, Object> request = new HashMap<>();
        request.put("themeId", themeId("Theme A"));
        request.put("name", "캐모");
        request.put("date", LocalDate.now().minusDays(1).toString());
        request.put("timeId", timeId("10:00:00"));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void 이미_예약된_시간에_중복_예약을_시도하면_409_예외가_발생한다() {
        Map<String, Object> request = new HashMap<>();
        request.put("themeId", themeId("Theme A"));
        request.put("name", "캐모");
        request.put("date", LocalDate.now().plusYears(1).toString());
        request.put("timeId", timeId("10:00:00"));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void X_User_Name_헤더_없이_예약을_삭제하면_400이_반환된다() {
        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId("User1"))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 예약_ID에_숫자가_아닌_값을_전달하면_400이_반환된다() {
        RestAssured.given().log().all()
                .header("X-User-Name", "User1")
                .when().delete("/reservations/invalid")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 예약자의_이름을_헤더로_전송하여_예약을_삭제할_수_있다() {
        RestAssured.given().log().all()
                .header("X-User-Name", "ScheduleTest")
                .when().delete("/reservations/" + reservationId("ScheduleTest"))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 타인의_예약을_삭제하려_하면_403이_반환된다() {
        RestAssured.given().log().all()
                .header("X-User-Name", "WrongUser")
                .when().delete("/reservations/" + reservationId("User1"))
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
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
                .body("size()", is(5));
    }

    @Test
    void 관리자는_특정_테마를_삭제할_수_있다() {
        RestAssured.given().log().all()
                .when().delete("/admin/themes/" + themeId("Theme E"))
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
                .when().delete("/admin/times/" + timeId("10:00:00"))
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void 특정_날짜와_테마의_스케줄_조회_시_예약_상태_isAvailable가_정확히_반환된다() {
        long themeAId = themeId("Theme A");
        long time10Id = timeId("10:00:00");
        long time11Id = timeId("11:00:00");

        RestAssured.given().log().all()
                .queryParam("date", "2099-12-31")
                .when().get("/times/" + themeAId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("themeId", is((int) themeAId))
                .body("date", is("2099-12-31"))
                .body("schedules.find { it.timeId == " + time10Id + " }.isAvailable", is(false))
                .body("schedules.find { it.timeId == " + time11Id + " }.isAvailable", is(true));
    }

    @Test
    void 본인의_예약_목록을_조회할_수_있다() {
        RestAssured.given().log().all()
                .header("X-User-Name", "User1")
                .when().get("/reservations/my")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }

    @Test
    void 한글_이름으로_본인의_예약_목록을_조회할_수_있다() {
        Map<String, Object> request = new HashMap<>();
        request.put("themeId", themeId("Theme A"));
        request.put("name", "홍길동");
        request.put("date", LocalDate.now().plusDays(1).toString());
        request.put("timeId", timeId("12:00:00"));
        RestAssured.given().contentType(ContentType.JSON).body(request)
                .when().post("/reservations").then().statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .header("X-User-Name", encodeHeader("홍길동"))
                .when().get("/reservations/my")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1))
                .body("[0].name", is("홍길동"));
    }

    @Test
    void 한글_이름으로_본인의_예약을_취소할_수_있다() {
        Map<String, Object> request = new HashMap<>();
        request.put("themeId", themeId("Theme A"));
        request.put("name", "홍길동");
        request.put("date", LocalDate.now().plusDays(1).toString());
        request.put("timeId", timeId("12:00:00"));
        long id = RestAssured.given().contentType(ContentType.JSON).body(request)
                .when().post("/reservations").jsonPath().getLong("id");

        RestAssured.given().log().all()
                .header("X-User-Name", encodeHeader("홍길동"))
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private static String encodeHeader(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    @Test
    void 이미_지난_예약을_취소하려_하면_422가_반환된다() {
        RestAssured.given().log().all()
                .header("X-User-Name", "User1")
                .when().delete("/reservations/" + reservationId("User1"))
                .then().log().all()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void 예약의_날짜와_시간을_변경할_수_있다() {
        Map<String, Object> request = new HashMap<>();
        request.put("date", LocalDate.now().plusDays(2).toString());
        request.put("timeId", timeId("11:00:00"));

        RestAssured.given().log().all()
                .header("X-User-Name", "ScheduleTest")
                .contentType(ContentType.JSON)
                .body(request)
                .when().patch("/reservations/" + reservationId("ScheduleTest"))
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 변경하려는_시간이_이미_차있으면_409가_반환된다() {
        long scheduleTestId = reservationId("ScheduleTest");
        long themeAId = themeId("Theme A");
        long time11Id = timeId("11:00:00");

        Map<String, Object> occupy = new HashMap<>();
        occupy.put("themeId", themeAId);
        occupy.put("name", "다른사람");
        occupy.put("date", "2099-12-31");
        occupy.put("timeId", time11Id);
        RestAssured.given().contentType(ContentType.JSON).body(occupy)
                .when().post("/reservations").then().statusCode(HttpStatus.CREATED.value());

        Map<String, Object> request = new HashMap<>();
        request.put("date", "2099-12-31");
        request.put("timeId", time11Id);

        RestAssured.given().log().all()
                .header("X-User-Name", "ScheduleTest")
                .contentType(ContentType.JSON)
                .body(request)
                .when().patch("/reservations/" + scheduleTestId)
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void 날짜_범위_내_예약_수_기준으로_랭킹이_내림차순_반환된다() {
        RestAssured.given().log().all()
                .queryParam("startDate", "2026-05-06")
                .queryParam("endDate", "2026-05-08")
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(4))
                .body("[0].name", is("Theme A"))
                .body("[1].name", is("Theme B"))
                .body("[2].name", is("Theme C"))
                .body("[3].name", is("Theme D"));
    }

    @Test
    void limit_파라미터를_2로_지정하면_상위_2개의_테마만_반환된다() {
        RestAssured.given().log().all()
                .queryParam("startDate", "2026-05-06")
                .queryParam("endDate", "2026-05-08")
                .queryParam("limit", 2)
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("[0].name", is("Theme A"))
                .body("[1].name", is("Theme B"));
    }

    @Test
    void 특정_날짜_startDate_endDate를_지정하여_랭킹을_조회할_수_있다() {
        RestAssured.given().log().all()
                .queryParam("startDate", "2026-05-05")
                .queryParam("endDate", "2026-05-06")
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].name", is("Theme A"));
    }

    private long themeId(String name) {
        return RestAssured.given()
                .get("/themes")
                .jsonPath()
                .getLong("find { it.name == '" + name + "' }.id");
    }

    private long timeId(String startAt) {
        return RestAssured.given()
                .get("/times")
                .jsonPath()
                .getLong("find { it.startAt == '" + startAt + "' }.id");
    }

    private long reservationId(String userName) {
        return RestAssured.given()
                .header("X-User-Name", userName)
                .get("/reservations/my")
                .jsonPath()
                .getLong("[0].id");
    }
}
