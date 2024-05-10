package roomescape.presentation.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.application.reservation.dto.request.ThemeRequest;
import roomescape.application.reservation.dto.response.ThemeResponse;

class ThemeAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테마를 생성한다.")
    void createThemeTest() {
        ThemeRequest request = new ThemeRequest("테마명", "테마 설명", "url");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void findAllThemesTest() {
        AcceptanceFixture.createTheme(new ThemeRequest("테마명1", "테마 설명1", "url1"));
        AcceptanceFixture.createTheme(new ThemeRequest("테마명2", "테마 설명2", "url2"));

        ThemeResponse[] responses = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(ThemeResponse[].class);

        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteThemeTest() {
        long themeId = AcceptanceFixture.createTheme(new ThemeRequest("테마명", "테마 설명", "url")).id();

        RestAssured.given().log().all()
                .when().delete("/themes/{id}", themeId)
                .then().log().all()
                .statusCode(204);
    }
}
