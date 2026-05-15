package roomescape.domain.time.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.global.config.TestClockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestClockConfig.class)
@DisplayName("사용자 시간의")
class TimeApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("예약 가능 시간 조회 api는")
    class GetAvailableTimesTest {

        @Test
        @DisplayName("예약 가능한 시간을 조회한다.")
        void 성공() {
            given()
                .params(Map.of(
                    "date", "2026-05-10",
                    "themeId", 1
                ))
                .when()
                .get("/api/times")
                .then()
                .statusCode(200);
        }

        @Test
        @DisplayName("필수 파라미터가 누락되면 400을 반환한다.")
        void 실패1() {
            given()
                .params(Map.of(
                    "date", "2026-05-10"
                ))
                .when()
                .get("/api/times")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("themeId"))
                .body("errors.find { it.field == 'themeId' }.message", equalTo("필수 필드가 누락되었습니다."));
        }

        @Test
        @DisplayName("themeId가 1보다 작으면 400을 반환한다.")
        void 실패2() {
            given()
                .params(Map.of(
                    "date", "2026-05-10",
                    "themeId", 0
                ))
                .when()
                .get("/api/times")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("themeId"))
                .body("errors.find { it.field == 'themeId' }.value", equalTo("0"))
                .body("errors.find { it.field == 'themeId' }.message", notNullValue());
        }

        @Test
        @DisplayName("날짜 형식이 잘못되면 400을 반환한다.")
        void 실패3() {
            String wrongDate = "2026/05/10";

            given()
                .params(Map.of(
                    "date", wrongDate,
                    "themeId", 1
                ))
                .when()
                .get("/api/times")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("date"))
                .body("errors.find { it.field == 'date' }.value", equalTo(wrongDate))
                .body("errors.find { it.field == 'date' }.message", equalTo("LocalDate 타입이어야 합니다."));
        }
    }
}
