package roomescape.controller;

import static org.hamcrest.Matchers.equalTo;

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
class ReservationTimeControllerTest {
    @DisplayName("API - 예약 시간 등록")
    @Test
    void API_예약_시간_등록() {
        final String createStartAt = "23:00";
        final Map<String, Object> params = new HashMap<>();
        params.put("startAt", createStartAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("startAt", equalTo(createStartAt));
    }

    @DisplayName("API - 예약 시간 조회")
    @Test
    void API_예약_시간_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", equalTo(13));
    }

    @DisplayName("API - 예약 시간 삭제")
    @Test
    void API_예약_시간_삭제() {
        final String createStartAt = "23:00";
        final Map<String, Object> params = new HashMap<>();
        params.put("startAt", createStartAt);

        final long id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("startAt", equalTo(createStartAt))
                .extract()
                .jsonPath()
                .getLong("id");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/" + id)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", equalTo(13));
    }
}
