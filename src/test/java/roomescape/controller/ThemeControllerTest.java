package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

class ThemeControllerTest extends BaseControllerTest {

    @TestFactory
    @DisplayName("테마를 생성, 조회, 삭제한다.")
    Stream<DynamicTest> themeControllerTests() {
        return Stream.of(
                DynamicTest.dynamicTest("테마를 생성한다.", this::addTheme),
                DynamicTest.dynamicTest("테마를 모두 조회한다.", this::getAllThemes),
                DynamicTest.dynamicTest("테마를 삭제한다.", this::deleteThemeById)
        );
    }

    void addTheme() {
        ThemeRequest request = new ThemeRequest("테마 이름", "테마 설명", "https://example.com/image.jpg");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .extract();

        ThemeResponse themeResponse = response.as(ThemeResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/themes/1");
            softly.assertThat(themeResponse)
                    .isEqualTo(new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com/image.jpg"));
        });
    }

    void getAllThemes() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .extract();

        List<ThemeResponse> themeResponses = response.jsonPath()
                .getList(".", ThemeResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(themeResponses).hasSize(1);
            softly.assertThat(themeResponses)
                    .containsExactly(new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com/image.jpg"));
        });
    }

    void deleteThemeById() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }
}
