package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TimeControllerTest {

    @Test
    @DisplayName("/times 요청 시 시간 정보 조회")
    void readReservations() {
        RestAssured.given().log().all()
            .when().get("/times")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(0));
    }

    @Test
    @DisplayName("시간 관리 페이지 내에서 시간 추가")
    void createReservation() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(getTestParamsWithReservationTime())
            .when().post("/times")
            .then().log().all()
            .statusCode(200);

        RestAssured.given().log().all()
            .when().get("/times")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    @DisplayName("시간 관리 페이지 내에서 예약 삭제")
    void deleteReservation() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(getTestParamsWithReservationTime())
            .when().post("/times")
            .then().log().all()
            .statusCode(200);

        RestAssured.given().log().all()
            .when().get("/times")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(1));

        RestAssured.given().log().all()
            .when().delete("/times/1")
            .then().log().all()
            .statusCode(200);

        RestAssured.given().log().all()
            .when().get("/times")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(0));
    }

    private Map<String, String> getTestParamsWithReservationTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");
        return params;
    }
}
