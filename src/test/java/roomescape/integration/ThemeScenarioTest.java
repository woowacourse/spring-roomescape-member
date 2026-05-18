package roomescape.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeScenarioTest {

    @Test
    @DisplayName("테마를 생성하면 목록 조회 시 생성된 테마를 조회할 수 있다.")
    void 테마_생성_후_목록_조회() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "무서운 이야기");
        params.put("description", "공포");
        params.put("url", "http://example.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("data.size()", is(1));
    }

    @Test
    @DisplayName("테마를 삭제하면 목록에서 제거된다.")
    void 테마_생성_후_삭제() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "무서운 이야기");
        params.put("description", "공포");
        params.put("url", "http://example.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("data.size()", is(0));
    }
}
