package roomescape.theme.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @DisplayName("테마 생성 API를 테스트합니다.")
    @Test
    void create_theme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "theme name");
        params.put("description", "theme description");
        params.put("thumbnailImgUrl", "theme img url");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마 삭제 API를 테스트합니다.")
    @Test
    void delete_theme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "theme name");
        params.put("description", "theme description");
        params.put("thumbnailImgUrl", "theme img url");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/themes/11")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 테마 삭제 시 404를 테스트합니다.")
    @Test
    void delete_theme_not_found() {
        RestAssured.given().log().all()
                .when().delete("/themes/100")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("존재하는 모든 테마 조회를 테스트합니다.")
    @Test
    void find_all_themes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("최근 일주일 간 인기 테마 조회 API를 테스트합니다.")
    @Test
    void find_popular_themes() {
        RestAssured.given().log().all()
                .when().get("/themes/popular-top-10")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
                .body("[0].id", equalTo(2))
                .body("[0].name", equalTo("SF 우주 탐험"))
                .body("[0].reservedCount", equalTo(10))
                .body("[4].id", equalTo(6))
                .body("[4].name", equalTo("비밀 연구소"))
                .body("[4].reservedCount", equalTo(6));
    }
}
