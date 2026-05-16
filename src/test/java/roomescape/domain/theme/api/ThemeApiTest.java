package roomescape.domain.theme.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.global.error.TypeMismatchMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("사용자 테마의")
class ThemeApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("테마 조회 api는")
    class GetThemesTest {

        @Test
        @DisplayName("테마를 조회한다.")
        void 성공() {
            given()
                .when()
                .get("/api/themes")
                .then()
                .statusCode(200);
        }
    }

    @Nested
    @DisplayName("인기 테마 조회 api는")
    class GetPopularThemesTest {

        @Test
        @DisplayName("인기 테마를 조회한다.")
        void 성공() {
            given()
                .params(Map.of(
                    "startDate", "2026-05-10",
                    "endDate", "2026-05-16",
                    "limit", 10
                ))
                .when()
                .get("/api/themes/popular")
                .then()
                .statusCode(200);
        }

        @Test
        @DisplayName("필수 파라미터가 누락되면 400을 반환한다.")
        void 실패1() {
            given()
                .params(Map.of(
                    "startDate", "2026-05-10",
                    "endDate", "2026-05-16"
                ))
                .when()
                .get("/api/themes/popular")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("limit"))
                .body("errors.find { it.field == 'limit' }.message", equalTo("필수 필드가 누락되었습니다."));
        }

        @Test
        @DisplayName("limit이 1보다 작으면 400을 반환한다.")
        void 실패2() {
            given()
                .params(Map.of(
                    "startDate", "2026-05-10",
                    "endDate", "2026-05-16",
                    "limit", 0
                ))
                .when()
                .get("/api/themes/popular")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("limit"))
                .body("errors.find { it.field == 'limit' }.value", equalTo("0"))
                .body("errors.find { it.field == 'limit' }.message", notNullValue());
        }

        @Test
        @DisplayName("날짜 형식이 잘못되면 400을 반환한다.")
        void 실패3() {
            String wrongDate = "2026/05/10";

            given()
                .params(Map.of(
                    "startDate", wrongDate,
                    "endDate", "2026-05-16",
                    "limit", 10
                ))
                .when()
                .get("/api/themes/popular")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("startDate"))
                .body("errors.find { it.field == 'startDate' }.value", equalTo(wrongDate))
                .body("errors.find { it.field == 'startDate' }.message", equalTo(
                    TypeMismatchMessage.from(LocalDate.class)));
        }
    }
}
