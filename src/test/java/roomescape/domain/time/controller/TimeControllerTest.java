package roomescape.domain.time.controller;

import static io.restassured.RestAssured.given;

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
class TimeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("getAvailableTimes 테스트")
    class GetAvailableTimesTest {

        @Test
        @DisplayName("가능한 시간 목록 조회 요청이면 200을 반환한다.")
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
    }
}