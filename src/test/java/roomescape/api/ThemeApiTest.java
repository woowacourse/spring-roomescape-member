package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테마 API 요구사항 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeApiTest {

    @Test
    @DisplayName("테마를 추가한다.")
    void createTheme() {
        // given
        Map<String, String> request = new HashMap<>();
        request.put("name", "귀신의 집");
        request.put("description", "무서워요");
        request.put("imageUrl", "/resources/image/ghost.png");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNull();assertThat(response.jsonPath().getObject("id", Integer.class)).isNotNull();
    }

    @Test
    @DisplayName("전체 테마 목록을 조회한다.")
    void getThemes() {
        // given
        createThemeHelper("귀신의 집", "무서워요", "/resources/image/1");
        createThemeHelper("물고기", "어푸", "/resources/image/2");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("")).hasSizeGreaterThanOrEqualTo(2);

        // JSON 응답 구조 검증
        assertThat(response.jsonPath().getList("id")).doesNotContainNull();
        assertThat(response.jsonPath().getList("name")).contains("귀신의 집", "물고기");
        assertThat(response.jsonPath().getList("description")).contains("무서워요", "어푸");
        assertThat(response.jsonPath().getList("imageUrl")).doesNotContainNull();
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteTheme() {
        // given
        int themaId = createThemeHelper("삭제할 테마", "삭제될 예정입니다", "/resources/image/delete");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("id", themaId)
                .when().delete("/themes/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private int createThemeHelper(String name, String description, String imageUrl) {
        Map<String, String> request = new HashMap<>();
        request.put("name", name);
        request.put("description", description);
        request.put("imageUrl", imageUrl);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .jsonPath().getInt("id");
    }
}
