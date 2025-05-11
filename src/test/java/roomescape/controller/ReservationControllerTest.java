package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Test
    @DisplayName("예약 등록 시 date 타입이 올바르지 않는다면 400에러를 반환한다.")
    void test1() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", "ㅇㅇㅇㅇ");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 날짜가 null 이라면 400에러를 반환한다.")
    void test2() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", null);
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 날짜가 공백 이라면 400에러를 반환한다.")
    void test3() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", "");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 시간이 null 이라면 400에러를 반환한다.")
    void test4() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", "");
        params.put("timeId", null);
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 테마가 null 이라면 400에러를 반환한다.")
    void test5() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", "");
        params.put("timeId", "1");
        params.put("themeId", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }
}