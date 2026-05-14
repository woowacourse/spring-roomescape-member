package roomescape.domain.theme.controller;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
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
class ThemeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("getThemes 테스트")
    class GetThemesTest {

        @Test
        @DisplayName("테마 조회 요청이면 200을 반환한다.")
        void 성공() {
            given()
                .when()
                .get("/api/themes")
                .then()
                .statusCode(200);
        }
    }

    @Nested
    @DisplayName("GetPopularThemes 테스트")
    class GetPopularThemesTest {

        @Test
        @DisplayName("인기 테마 목록 조회 요청이면 200을 반환한다.")
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
    }
}