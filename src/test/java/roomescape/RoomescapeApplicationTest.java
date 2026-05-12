package roomescape;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
    void 예약자의_이름을_헤더로_전송하여_예약을_삭제할_수_있다() {
        RestAssured.given().log().all()
                .header("X-User-Name", "User1")
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
                .body("size()", is(5));
    }

    @Test
    void 관리자는_특정_테마를_삭제할_수_있다() {
        RestAssured.given().log().all()
                .when().delete("/admin/themes/5")
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
                .statusCode(HttpStatus.OK.value()).body("size()", is(4))
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
}
