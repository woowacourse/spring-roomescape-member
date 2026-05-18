package roomescape.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeScenarioTest {

    @Test
    @DisplayName("예약 시간을 생성하면 목록 조회 시 생성된 시간을 조회할 수 있다.")
    void 예약_시간_생성_후_목록_조회() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("data.size()", is(1));
    }

    @Test
    @DisplayName("예약 시간을 수정하면 변경된 시간을 조회할 수 있다.")
    void 예약_시간_생성_후_수정() {
        Map<String, String> createParams = new HashMap<>();
        createParams.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(createParams)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(200);

        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("startAt", "12:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateParams)
                .when().put("/admin/times/1")
                .then().log().all()
                .statusCode(200)
                .body("data.startAt", is("12:00:00"));
    }

    @Test
    @DisplayName("예약 시간을 수정하면 목록 조회 시 변경된 시간을 확인할 수 있다.")
    void 예약_시간_수정_후_목록_조회() {
        Map<String, String> createParams = new HashMap<>();
        createParams.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(createParams)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(200);

        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("startAt", "12:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateParams)
                .when().put("/admin/times/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("data[0].startAt", is("12:00:00"));
    }

    @Test
    @DisplayName("예약 시간을 삭제하면 목록에서 제거된다.")
    void 예약_시간_생성_후_삭제() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("data.size()", is(0));
    }
}
