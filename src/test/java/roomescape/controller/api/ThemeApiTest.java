package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.dto.theme.ThemeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeApiTest {

    @Test
    @DisplayName("테마 생성 테스트")
    void createThemeWithStatus201Test() {
        var themeRequest = new ThemeRequest(
                "스테이지",
                "인기 아이돌 실종 사건",
                "무엇보다 무섭다"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
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
        var themeRequest = new ThemeRequest(
                "삭제 테스트용 테마",
                "삭제 테스트용 테마",
                "삭제 테스트용 테마"
        );

        int themId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().path("id");

        RestAssured.given().log().all()
                .when().delete("/themes/" + themId)
                .then().log().all()
                .statusCode(204);
    }
}
