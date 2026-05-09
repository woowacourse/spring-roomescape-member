package roomescape.theme.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/create_theme.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminThemeControllerTest {

    @Test
    void 테마를_추가한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "테마");
        params.put("description", "테마 설명");
        params.put("thumbnailUrl", "https://example.com/theme.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("name", is("테마"))
                .body("description", is("테마 설명"))
                .body("thumbnailUrl", is("https://example.com/theme.png"))
                .body("runtime", is(1));
    }

    @Test
    void 테마를_삭제한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "테마");
        params.put("description", "테마 설명");
        params.put("thumbnailUrl", "https://example.com/theme.png");

        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().delete("/admin/themes/" + id)
                .then().log().all()
                .statusCode(204);
    }
}
