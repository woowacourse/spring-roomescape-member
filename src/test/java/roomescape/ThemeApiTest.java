package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeApiTest {

    @Test
    @DisplayName("테마 생성 테스트")
    void createThemeWithStatus201Test() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "스테이지");
        params.put("description", "인기 아이돌 실종 사건");
        params.put("thumbnail", "무엇보다 무섭다");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("테마 조회 테스트")
    void searchThemeWithStatus200Test() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("인기 테마 조회 테스트")
    void searchPopularThemeWithStatus200() {
        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("테마 삭제 테스트")
    void deleteThemeWithStatus204Test() {
        RestAssured.given().log().all()
                .when().delete("/themes/3")
                .then().log().all()
                .statusCode(204);
    }
}
