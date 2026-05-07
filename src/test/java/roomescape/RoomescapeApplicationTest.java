package roomescape;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

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
                .when().post("/admin/themes")
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
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
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
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201);

        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "귀신찾기");
        themeParams.put("description", "귀신찾기을 찾는 테마입니다.");
        themeParams.put("imageUrl", "https://image.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/admin/themes")
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
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @Sql("/data.sql")
    void 인기_테마_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations/popular-themes")
                .then().log().all()
                .statusCode(200)
                .body("id", contains(1, 2, 3, 6, 5, 4, 8, 7, 10, 9));
    }
}
