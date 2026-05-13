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
    void 테마_생성시_성공하면_201을_반환한다() {
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
    void 테마_삭제시_성공하면_204를_반환한다() {
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
    void 테마_삭제시_테마가_존재하지_않으면_404를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams())
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().delete("/api/v1/admin/themes/2")
                .then().log().all()
                .statusCode(404)
                .body("errorCode", is("THEME404_001"));
    }

    @Test
    void 테마_삭제시_예약이_존재하면_409를_반환한다() {
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
                .statusCode(409)
                .body("errorCode", is("THEME409_001"));
    }

    @Test
    void 테마_생성시_이름이_비어있으면_400을_반환한다() {
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", "");
        themeParams.put("description", "이든이 귀신으로 나옴");
        themeParams.put("imgUrl", "https://images.example.com/themes/horror-house.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 테마_생성시_이름이_두글자_미만이면_400을_반환한다() {
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", "한");
        themeParams.put("description", "이든이 귀신으로 나옴");
        themeParams.put("imgUrl", "https://images.example.com/themes/horror-house.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 테마_생성시_이름이_백글자_초과면_400을_반환한다() {
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", "한".repeat(101));
        themeParams.put("description", "이든이 귀신으로 나옴");
        themeParams.put("imgUrl", "https://images.example.com/themes/horror-house.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 테마_생성시_설명이_비어있으면_400을_반환한다() {
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", "이든의 공포 하우스");
        themeParams.put("description", "");
        themeParams.put("imgUrl", "https://images.example.com/themes/horror-house.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 테마_생성시_설명이_100자를_초과하면_400을_반환한다() {
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", "이든의 공포 하우스");
        themeParams.put("description", "*".repeat(200));
        themeParams.put("imgUrl", "https://images.example.com/themes/horror-house.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 테마_생성시_imgUrl이_비어있으면_400을_반환한다() {
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", "이든의 공포 하우스");
        themeParams.put("description", "이든이 귀신으로 나옴");
        themeParams.put("imgUrl", " ");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 테마_생성시_imgUrl_형식이_잘못되면_400을_반환한다() {
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", "이든의 공포 하우스");
        themeParams.put("description", "이든이 귀신으로 나옴");
        themeParams.put("imgUrl", "링크~");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
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
