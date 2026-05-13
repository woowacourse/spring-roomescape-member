package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminThemeControllerTest {

    @Test
    void 테마_추가() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams())
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/api/v1/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 테마_삭제() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams())
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/api/v1/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/api/v1/admin/themes/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/api/v1/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 예약이_존재하는_테마를_삭제하면_409를_반환한다() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/admin/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams())
                .when().post("/api/v1/admin/themes")
                .then().statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/api/v1/admin/themes/1")
                .then().log().all()
                .statusCode(409);
    }

    private Map<String, Object> themeParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "이든의 공포 하우스");
        params.put("description", "이든이 귀신으로 나옴");
        params.put("imgUrl", "https://images.example.com/themes/horror-house.jpg");
        return params;
    }

    private Map<String, Object> reservationParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1L);
        params.put("themeId", 1L);
        return params;
    }
}
