package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeApiTest {

    @Test
    void 테마_조회_빈목록() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 테마_추가() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "공포");
        params.put("description", "무서운 테마");
        params.put("thumbnailImageUrl", "https://example.com/horror.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", is("공포"))
                .body("description", is("무서운 테마"))
                .body("thumbnailImageUrl", is("https://example.com/horror.jpg"));
    }

    @Test
    void 테마_추가_후_조회() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "추리");
        params.put("description", "단서를 찾아라");
        params.put("thumbnailImageUrl", "https://example.com/mystery.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("추리"));
    }

    @Test
    void 테마_추가_및_삭제() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "SF");
        params.put("description", "우주에서 탈출");
        params.put("thumbnailImageUrl", "https://example.com/sf.jpg");

        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().get("id");

        RestAssured.given().log().all()
                .when().delete("/themes/" + id)
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
