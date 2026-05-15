package roomescape.domain.theme.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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
@DisplayName("관리자 테마의")
class AdminThemeApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("테마 생성 api는")
    class SaveThemeTest {

        @Test
        @DisplayName("테마를 생성한다.")
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

        @Test
        @DisplayName("이름이 빈 문자열이면 400을 반환한다.")
        void 실패1() {
            given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "name", "",
                    "description", "새 테마 설명",
                    "imageUrl", "imageUrl"
                ))
                .when()
                .post("/api/admin/themes")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("name"))
                .body("errors.find { it.field == 'name' }.value", equalTo(""))
                .body("errors.find { it.field == 'name' }.message", notNullValue());
        }

        @Test
        @DisplayName("필수 필드가 누락되면 400을 반환한다.")
        void 실패2() {
            given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "name", "새 테마",
                    "imageUrl", "imageUrl"
                ))
                .when()
                .post("/api/admin/themes")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("description"))
                .body("errors.find { it.field == 'description' }.value", nullValue())
                .body("errors.find { it.field == 'description' }.message", notNullValue());
        }
    }

    @Nested
    @DisplayName("테마 삭제 api는")
    class DeleteThemeTest {

        @Test
        @DisplayName("테마를 삭제한다.")
        void 성공() {
            Long id = createTheme();

            given()
                .when()
                .delete("/api/admin/themes/{id}", id)
                .then()
                .statusCode(204);
        }

        @Test
        @DisplayName("요청 경로 변수가 잘못되면 400을 반환한다.")
        void 실패1() {
            Object wrongId = "a";

            given()
                .when()
                .delete("/api/admin/themes/{id}", wrongId)
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("id"))
                .body("errors.find { it.field == 'id' }.value", equalTo(wrongId))
                .body("errors.find { it.field == 'id' }.message", equalTo("Long 타입이어야 합니다."));
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
