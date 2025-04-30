package roomescape.theme.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {
    @Test
    void 테마_추가_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨1");
        params.put("description", "레벨1 성공하자");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 테마_전체조회_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨1");
        params.put("description", "레벨1 성공하자");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 테마_삭제_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨1");
        params.put("description", "레벨1 성공하자");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/themes");
    }

}
