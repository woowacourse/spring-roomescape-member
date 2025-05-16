package roomescape.controller;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.theme.dto.request.ThemeRequestDto;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @DisplayName("테마 조회 api 테스트")
    @Test
    void get_themes() {
        given().log().all()
            .when().get("/themes")
            .then().log().all()
            .statusCode(200);
    }

    @DisplayName("테마 순위별 조회 api 테스트")
    @Test
    void get_themes_rank() {
        given().log().all()
            .when().get("/themes/ranks")
            .then().log().all()
            .statusCode(200);
    }

    @DisplayName("테마 추가 API 테스트")
    @Test
    void post_theme() {
        ThemeRequestDto request = new ThemeRequestDto("테마", "공포", "http://aaa");
        given()
            .contentType("application/json")
            .body(request)
            .when()
            .post("/themes")
            .then()
            .statusCode(201)
            .body("name", equalTo("테마"))
            .body("description", equalTo("공포"))
            .body("thumbnail", equalTo("http://aaa"));
    }

    @DisplayName("테마 삭제 API 테스트")
    @Test
    void delete_theme_by_id() {
        ThemeRequestDto request = new ThemeRequestDto("테마", "공포", "http://aaa");
        given()
            .contentType("application/json")
            .body(request)
            .when()
            .post("/themes")
            .then()
            .statusCode(201)
            .body("name", equalTo("테마"))
            .body("description", equalTo("공포"))
            .body("thumbnail", equalTo("http://aaa"));

        RestAssured.given().log().all()
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(204);
    }
}
