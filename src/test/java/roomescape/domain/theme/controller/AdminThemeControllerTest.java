package roomescape.domain.theme.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminThemeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("saveTheme 테스트")
    class SaveThemeTest {

        @Test
        @DisplayName("테마 생성 요청이면 201을 반환한다.")
        void 성공() {
            given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "name", "새 테마",
                    "description", "새 테마 설명",
                    "imageUrl", "imageUrl"
                ))
                .when()
                .post("/api/admin/themes")
                .then()
                .statusCode(201)
                .body("name", equalTo("새 테마"));
        }
    }

    @Nested
    @DisplayName("deleteTheme 테스트")
    class DeleteThemeTest {

        @Test
        @DisplayName("테마 삭제 요청이면 204를 반환한다.")
        void 성공() {
            Long id = createTheme();

            given()
                .when()
                .delete("/api/admin/themes/{id}", id)
                .then()
                .statusCode(204);
        }
    }

    private Long createTheme() {
        return given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "name", "삭제할 테마",
                "description", "삭제할 테마 설명",
                "imageUrl", "imageUrl"
            ))
            .when()
            .post("/api/admin/themes")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getLong("id");
    }
}
