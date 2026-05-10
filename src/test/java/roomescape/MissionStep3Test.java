package roomescape;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStep3Test {

    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");
        params.put("finishAt", "11:00");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
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
            .statusCode(204);
    }

    @Test
    void 예약과_시간_연결() {
        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "테마1");
        themeParams.put("description", "테마 설명");
        themeParams.put("imageUrl", "https://example.com/image.jpg");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(themeParams)
            .when().post("/admin/themes")
            .then().log().all()
            .statusCode(200);

        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", "10:00");
        timeParams.put("finishAt", "11:00");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(timeParams)
            .when().post("/times")
            .then().log().all()
            .statusCode(200);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(reservation)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(200);

        RestAssured.given().log().all()
            .when().get("/reservations?date=2023-08-05&themeId=1")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(0));
    }

}