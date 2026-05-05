package roomescape.reservation.presentation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ReservationTimeControllerTest {
    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

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
    void 특정날짜와_테마에_예약_가능_시간을_조회_API(){
        //1. 시간 초기화
        Map<String, String> time1 = new HashMap<>();
        time1.put("startAt", "10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time1)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        Map<String, String> time2 = new HashMap<>();
        time2.put("startAt", "11:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time2)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        Map<String, String> time3 = new HashMap<>();
        time3.put("startAt", "12:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time3)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        //2. 테마 초기화
        Map<String, String> firstTheme = new HashMap<>();
        firstTheme.put("name", "꿀잼 방탈출");
        firstTheme.put("description", "재밌는 분위기의 방탈출");
        firstTheme.put("thumbnailUrl", "https://example.com/theme_happy.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(firstTheme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        //3. 예약 생성
        Map<String, Object> reservation1 = new HashMap<>();
        reservation1.put("name", "브라운");
        reservation1.put("date", "2023-08-05");
        reservation1.put("timeId", 1);
        reservation1.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation1)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> reservation2 = new HashMap<>();
        reservation2.put("name", "브라운");
        reservation2.put("date", "2023-08-05");
        reservation2.put("timeId", 2);
        reservation2.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation2)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        //4. 특정날짜와_테마에_예약_가능_시간을_조회
        Map<String, Object> options = new HashMap<>();
        options.put("date", "2023-08-05");
        options.put("themeId", 1);

        RestAssured.given().log().all()
                .params(options)
                .when().get("/times")
                .then().log().all()
                .statusCode(200);
    }
}
