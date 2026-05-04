package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    private Map<String, Object> themeParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "이든의 공포 하우스");
        params.put("description", "이든이 귀신으로 나옴");
        params.put("imgUrl", "링크~");
        return params;
    }

    private Map<String, Object> reservationParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);
        return params;
    }

    @Test
    void 테마_추가() {
        Map<String, Object> adminThemeParams = themeParams();
        adminThemeParams.put("userName", "ADMIN");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminThemeParams)
                .when().post("/api/v1/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    @Test
    void 테마_추가시_관리자가_아닌경우_401을_반환한다() {
        Map<String, Object> userThemeParams = themeParams();
        userThemeParams.put("userName", "정콩이");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(userThemeParams)
                .when().post("/api/v1/themes")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    void 테마_삭제() {
        Map<String, Object> adminThemeParams = themeParams();
        adminThemeParams.put("userName", "ADMIN");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminThemeParams)
                .when().post("/api/v1/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("userName", "ADMIN"))
                .when().delete("/api/v1/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 테마_삭제시_관리자가_아닌경우_401을_반환한다() {
        Map<String, Object> adminThemeParams = themeParams();
        adminThemeParams.put("userName", "ADMIN");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminThemeParams)
                .when().post("/api/v1/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("userName", "정콩이"))
                .when().delete("/api/v1/themes/1")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    void 예약이_존재하는_테마를_삭제하면_409를_반환한다() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/times")
                .then().statusCode(201);

        Map<String, Object> adminThemeParams = themeParams();
        adminThemeParams.put("userName", "ADMIN");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminThemeParams)
                .when().post("/api/v1/themes")
                .then().statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("userName", "ADMIN"))
                .when().delete("/api/v1/themes/1")
                .then().log().all()
                .statusCode(409);
    }
}
