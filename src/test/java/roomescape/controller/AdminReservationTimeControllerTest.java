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
public class AdminReservationTimeControllerTest {

    @BeforeEach
    void setUp() {
        createTime("10:00");
        createTime("11:00");
        createTime("12:00");

        createTheme("이든의 공포 하우스", "이든이 귀신으로 나옴");
        createTheme("정콩이의 방탈출", "니는 못나간다");
    }

    @Test
    void 시간_추가() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "13:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/admin/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/v1/admin/times/4")
                .body("id", is(4));
    }

    @Test
    void 시간_관리_API() {
        RestAssured.given().log().all()
                .when().get("/api/v1/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3))
                .body("[0].startAt", is("10:00"))
                .body("[1].startAt", is("11:00"))
                .body("[2].startAt", is("12:00"));

        RestAssured.given().log().all()
                .when().delete("/api/v1/admin/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 사용자는_예약_가능한_시간을_선택하여_예약_가능하다() {
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("timeId", 2)))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/v1/reservations/2");
    }

    @Test
    void 사용자가_예약된_시간을_선택하여_예약할경우_409를_반환한다() {
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("timeId", 1)))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(409)
                .body("errorCode", is("DUPLICATE_RESERVATION"));
    }


    @Test
    void 사용자는_같은_날짜_시간이라도_테마가_다르면_각각_예약_가능하다() {
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("themeId", 2)))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/v1/reservations/2");
    }

    @Test
    void 존재하지_않는_예약시간을_삭제하면_404를_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/api/v1/admin/times/4")
                .then().log().all()
                .statusCode(404)
                .body("errorCode", is("RESERVATION_TIME_NOT_FOUND"));
    }

    @Test
    void 예약이_존재하는_예약시간을_삭제하면_409를_반환한다() {
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .when().delete("/api/v1/admin/times/1")
                .then().log().all()
                .statusCode(409)
                .body("errorCode", is("RESERVATION_TIME_IN_USE"));

    }

    @Test
    void 시간_추가_시_필수_파라미터가_누락되면_400을_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of())
                .when().post("/api/v1/admin/times")
                .then().log().all()
                .statusCode(400)
                .body("status", is(400))
                .body("errorCode", is("INVALID_INPUT_VALUE"));
    }

    @Test
    void 시간_삭제_시_잘못된_타입의_ID를_전달하면_400을_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/api/v1/admin/times/invalid-id")
                .then().log().all()
                .statusCode(400)
                .body("status", is(400))
                .body("errorCode", is("TYPE_MISMATCH"))
                .body("message", containsString("타입이 일치하지 않습니다"));
    }

    @Test
    void 시간_삭제_시_ID를_입력하지_않으면_404를_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/api/v1/admin/times/")
                .then().log().all()
                .statusCode(404)
                .body("status", is(404))
                .body("errorCode", is("PATH_NOT_FOUND"))
                .body("message", containsString("요청하신 경로를 찾을 수 없습니다"));
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
        return params;
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
