package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.theme.ThemeSaveRequest;

class ThemeAcceptanceTest extends ApiAcceptanceTest {

    @Test
    @DisplayName("테마를 성공적으로 생성하면 201을 응답한다.")
    void respondCreatedWhenCreateTheme() {
        final ThemeSaveRequest request = new ThemeSaveRequest("호러", "매우 무섭습니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("테마 목록을 성공적으로 조회하면 200을 응답한다.")
    void respondOkWhenFindThemes() {
        saveTheme();

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("인기 테마를 성공적으로 조히하면 200을 응답한다.")
    void respondOkWhenFindPopularThemes() {
        saveTheme();

        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("테마를 성공적으로 삭제하면 204를 응답한다.")
    void respondNoContentWhenDeleteThemes() {
        final Long themeId = saveTheme();

        RestAssured.given().log().all()
                .when().delete("/themes/" + themeId)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하면 400을 응답한다.")
    void respondBadRequestWhenDeleteNotExistingTheme() {
        saveTheme();
        final Long notExistingThemeId = 2L;

        RestAssured.given().log().all()
                .when().delete("/themes/" + notExistingThemeId)
                .then().log().all()
                .statusCode(400);
    }
}
