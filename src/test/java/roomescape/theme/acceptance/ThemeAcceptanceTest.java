package roomescape.theme.acceptance;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.theme.dto.request.ThemeRequest.ThemeCreateRequest;

// @formatter:off
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeAcceptanceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("테마를 생성한다.")
    void createTheme() throws Exception {
        // given
        var request = new ThemeCreateRequest(
                "미소",
                "미소 테마",
                "https://miso.com"
        );

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/themes")
        .then()
                .statusCode(201)
                .body("id", equalTo(1))
                .body("name", equalTo("미소"))
                .body("description", equalTo("미소 테마"))
                .body("thumbnail", equalTo("https://miso.com"));
    }

    @Test
    @DisplayName("중복되는 테마 이름이 있을 경우 생성할 수 없다.")
    void createThemeWithDuplicateName() throws Exception {
        // given
        var request1 = new ThemeCreateRequest(
                "미소",
                "미소 테마",
                "https://miso.com"
        );
        var request2 = new ThemeCreateRequest(
                "미소",
                "미소 테마2",
                "https://miso2.com"
        );

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request1))
        .when()
                .post("/themes")
        .then()
                .statusCode(201);

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request2))
        .when()
                .post("/themes")
        .then()
                .statusCode(409)
                .body("message", equalTo("이미 존재하는 테마 이름입니다."));
    }

    @Test
    @DisplayName("모든 테마를 조회한다.")
    void getAllThemes() throws Exception {
        // given
        var request1 = new ThemeCreateRequest(
                "미소",
                "미소 테마",
                "https://miso.com"
        );
        var request2 = new ThemeCreateRequest(
                "우테코",
                "우테코 테마",
                "https://wooteco.com"
        );

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request1))
        .when()
                .post("/themes")
        .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request2))
        .when()
                .post("/themes")
        .then()
                .statusCode(201);

        // when & then
        given()
        .when()
                .get("/themes")
        .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].name", equalTo("미소"))
                .body("[1].name", equalTo("우테코"));
    }

    @Test
    @DisplayName("인기 있는 테마를 조회한다.")
    void getPopularThemes() throws Exception {
        // given
        var request1 = new ThemeCreateRequest(
                "미소",
                "미소 테마",
                "https://miso.com"
        );
        var request2 = new ThemeCreateRequest(
                "우테코",
                "우테코 테마",
                "https://wooteco.com"
        );

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request1))
        .when()
                .post("/themes")
        .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request2))
        .when()
                .post("/themes")
        .then()
                .statusCode(201);

        // when & then
        given()
                .param("count", 2)
        .when()
                .get("/themes/popular")
        .then()
                .statusCode(200)
                .body("$", hasSize(2));
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteTheme() throws Exception {
        // given
        var request = new ThemeCreateRequest(
                "미소",
                "미소 테마",
                "https://miso.com"
        );

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/themes")
        .then()
                .statusCode(201);

        // when & then
        given()
        .when()
                .delete("/themes/1")
        .then()
                .statusCode(204);

        given()
        .when()
                .get("/themes")
        .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하면 예외가 발생한다.")
    void deleteNonExistentTheme() {
        // when & then
        given()
        .when()
                .delete("/themes/1")
        .then()
                .statusCode(404)
                .body("message", equalTo("존재하지 않는 테마입니다."));
    }
}
