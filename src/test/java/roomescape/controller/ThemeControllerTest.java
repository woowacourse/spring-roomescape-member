package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @Test
    @DisplayName("API - 전체 테마 조회")
    void API_전체_테마_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("API - Top 10 테마 조회")
    void API_예약_시간_조회() {
        Map<String,Object> params = new HashMap<>();
        params.put("condition", "popular");
        params.put("size", "10");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .params(params)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("API - 특정 id의 테마 시간 조회")
    void API_특정_id의_테마_시간_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("id", 0)
                .queryParam("date", "2026-04-29")
                .when().get("/themes/{id}/times")
                .then().log().all()
                .statusCode(200);
    }
}
