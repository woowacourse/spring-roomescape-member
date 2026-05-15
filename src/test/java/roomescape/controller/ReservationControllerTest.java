package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @BeforeEach
    void setUp() {
        createTime("10:00");
        createTheme("이든의 공포 하우스", "이든이 귀신으로 나옴");
    }

    @Test
    void 빈_예약_목록_조회() {
        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 예약_추가() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/v1/reservations/1")
                .body("id", is(1));
    }

    @Test
    void 예약_추가_시_예약_날짜가_과거라면_400을_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("date", "2026-01-01")))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("INVALID_RESERVATION_TIME"));
    }

    @Test
    void 예약_추가_시_필수_파라미터가_누락되면_400을_반환한다() {
        Map<String, Object> invalidParams = new HashMap<>();
        invalidParams.put("name", "브라운");
        invalidParams.put("timeId", 1);
        invalidParams.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(invalidParams)
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("status", is(400))
                .body("errorCode", is("INVALID_INPUT_VALUE"));
    }

    @Test
    void 예약_추가_시_해당_날짜_시간_테마에_예약이_있다면_409를_반환한다() {
        Map<String, Object> params = reservationParams();
        createReservation(params);
        params.put("name", "브리");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(409)
                .body("status", is(409))
                .body("errorCode", is("DUPLICATE_RESERVATION"));
    }

    @Test
    void 예약_목록_조회() {
        createReservation(reservationParams());
        createTime("11:00");
        createReservation(reservationParams(Map.of("name", "브리", "timeId", 2, "themeId", 1)));

        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].name", is("브라운"))
                .body("[0].date", is("2026-12-31"))
                .body("[0].time.id", is(1))
                .body("[0].time.startAt", is("10:00"))
                .body("[0].themeId", is(1))
                .body("[1].id", is(2))
                .body("[1].name", is("브리"))
                .body("[1].date", is("2026-12-31"))
                .body("[1].time.id", is(2))
                .body("[1].time.startAt", is("11:00"))
                .body("[1].themeId", is(1));
    }

    @Test
    void 예약_삭제() {
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .when().delete("/api/v1/reservations/1")
                .then().log().all()
                .statusCode(204);
        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_404를_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/api/v1/reservations/1")
                .then().log().all()
                .statusCode(404)
                .body("errorCode", is("RESERVATION_NOT_FOUND"));
    }

    @Test
    void 예약_삭제_시_잘못된_타입의_ID를_전달하면_400을_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/api/v1/reservations/invalid-id")
                .then().log().all()
                .statusCode(400)
                .body("status", is(400))
                .body("errorCode", is("TYPE_MISMATCH"))
                .body("message", containsString("타입이 일치하지 않습니다"));
    }

    @Test
    void 예약_삭제_시_ID를_입력하지_않으면_404를_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/api/v1/reservations/")
                .then().log().all()
                .statusCode(404)
                .body("status", is(404))
                .body("errorCode", is("PATH_NOT_FOUND"))
                .body("message", containsString("요청하신 경로를 찾을 수 없습니다"));
    }

    @Test
    void 예약_가능_시간_조회() {
        createReservation(reservationParams());
        createTime("11:00");
        createTime("12:00");

        RestAssured.given().log().all()
                .when().get("/api/v1/reservations/available-times?date=2026-12-31&themeId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3))
                .body("[0].available", is(false))
                .body("[1].available", is(true))
                .body("[2].available", is(true));
    }

    @Test
    void 예약_가능_시간_조회_시_필수_파라미터가_누락되면_400을_반환한다() {
        RestAssured.given().log().all()
                .when().get("/api/v1/reservations/available-times?themeId=1")
                .then().log().all()
                .statusCode(400)
                .body("status", is(400))
                .body("errorCode", is("INVALID_PARAMETER_CONDITION"));
    }

    @Test
    void 형식이_잘못된_JSON을_전달하면_400을_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"브라운\", \"date\": \"2026-12-31\", \"timeId\": 1, \"themeId\": 1") // 닫는 괄호 누락
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("MALFORMED_JSON"));
    }

    @Test
    void 지원하지_않는_미디어_타입으로_요청하면_415를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.TEXT)
                .body("plain text body")
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(415)
                .body("errorCode", is("UNSUPPORTED_MEDIA_TYPE"));
    }

    private Map<String, Object> reservationParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-12-31");
        params.put("timeId", 1);
        params.put("themeId", 1);
        return params;
    }

    private Map<String, Object> reservationParams(Map<String, Object> overrides) {
        Map<String, Object> params = reservationParams();
        params.putAll(overrides);
        return Map.copyOf(params);
    }

    private void createTime(String startAt) {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", startAt);

        RestAssured.given().contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/admin/times");
    }

    private void createTheme(String name, String description) {
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", name);
        themeParams.put("description", description);
        themeParams.put("imgUrl", "링크~");
        themeParams.put("userName", "ADMIN");

        RestAssured.given().contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes");
    }

    private void createReservation(Map<String, Object> params) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/v1/reservations");
    }
}