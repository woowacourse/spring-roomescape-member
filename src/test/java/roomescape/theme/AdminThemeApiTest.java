package roomescape.theme;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class AdminThemeApiTest extends IntegrationTestSupport {

    @Test
    @DisplayName("관리자는 테마를 생성할 수 있다")
    void createTheme() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "테마1",
                        "description", "테마1 설명",
                        "thumbnailUrl", "테마1 썸네일"
                ))
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("name", is("테마1"))
                .body("description", is("테마1 설명"))
                .body("thumbnailUrl", is("테마1 썸네일"))
                .body("isActive", is(false));
    }

    @Test
    @DisplayName("관리자는 전체 테마 목록을 조회할 수 있다")
    void findThemes() {
        createTheme("테마1");

        RestAssured.given().log().all()
                .when().get("/admin/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("테마1"));
    }

    @Test
    @DisplayName("관리자는 테마를 단건 조회할 수 있다")
    void findTheme() {
        Long themeId = createTheme("테마1");

        RestAssured.given().log().all()
                .when().get("/admin/themes/{id}", themeId)
                .then().log().all()
                .statusCode(200)
                .body("id", is(themeId.intValue()))
                .body("name", is("테마1"));
    }

    @Test
    @DisplayName("관리자는 테마 활성화 상태를 변경할 수 있다")
    void updateThemeStatus() {
        Long themeId = createTheme("테마1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("isActive", true))
                .when().patch("/admin/themes/{id}", themeId)
                .then().log().all()
                .statusCode(200)
                .body("id", is(themeId.intValue()))
                .body("isActive", is(true));
    }
}
