package roomescape.admin;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminThemeControllerTest {
    @Test
    void 테마_관리() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "재밌는방탈출");
        params.put("description", "재밌는방탈출");
        params.put("thumbnail", "s3.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/admin/themes/6")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 빈값으로_테마_추가시_400() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("description", "재밌는방탈출");
        params.put("thumbnail", "s3.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 존재하지_않는_테마_삭제시_404() {
        RestAssured.given().log().all()
                .when().delete("/admin/themes/0")
                .then().log().all()
                .statusCode(404);
    }
}
