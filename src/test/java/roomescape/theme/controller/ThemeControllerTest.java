package roomescape.theme.controller;

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
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 테마 삭제 시 404를 테스트합니다.")
    @Test
    void delete_theme_not_found() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(404);
    }
}
