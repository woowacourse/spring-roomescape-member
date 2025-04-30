package roomescape;

import static org.hamcrest.Matchers.containsString;

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
public class ReservationTimeControllerTest {
    void Test_ReservationTime_Post() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("시작 시간 형식 검증")
    void test1() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "미친형식");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("시작 시간 null 검증")
    void test2() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] "));
    }

    @Test
    @DisplayName("예약된 시간을 삭제할 수 없음")
    void test3() {
        // given
        Test_ReservationTime_Post();

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2025-08-05");
        params.put("timeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)

                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // when

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/2")
                .then().log().all()
                .statusCode(400);
    }

}
