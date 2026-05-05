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
class RoomescapeApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void 테마_추가하고_조회() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "귀신찾기");
        params.put("description", "귀신찾기을 찾는 테마입니다.");
        params.put("imageUrl", "https://image.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 테마_삭제() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "귀신찾기");
        params.put("description", "귀신찾기을 찾는 테마입니다.");
        params.put("imageUrl", "https://image.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 예약_생성() {
        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParams)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "귀신찾기");
        themeParams.put("description", "귀신찾기을 찾는 테마입니다.");
        themeParams.put("imageUrl", "https://image.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

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
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

}
