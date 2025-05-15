package roomescape.theme.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {
    @Test
    void 테마_추가_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "테마6");
        params.put("description", "테마6 성공하자");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        Long id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().as(Long.class);

        assertThat(id).isEqualTo(6L);
    }

    @Test
    void 테마_전체조회_테스트() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(5));
    }

    @Test
    void 테마_삭제_테스트() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/5")
                .then().log().all()
                .statusCode(204);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().body("size()", is(4));
    }

    @Test
    void 인기테마_상위10개_조회_테스트() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/themes/top10")
                .then().statusCode(200)
                .body("size()", is(0));
    }
}
