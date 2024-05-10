package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.BDDMockito.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.theme.ThemeCreateRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @TestFactory
    @DisplayName("테마를 생성하고 조회하고 삭제한다.")
    Collection<DynamicTest> createAndReadAndDelete() {
        ThemeCreateRequest params = ThemeCreateRequest.of("방탈출1", "1번 방탈출", "썸네일1");

        return List.of(
                dynamicTest("테마를 생성한다", () ->
                        RestAssured.given().log().all()
                                .contentType(ContentType.JSON)
                                .body(params)
                                .when().post("/themes")
                                .then().log().all()
                                .statusCode(201)
                                .header("Location", "/themes/1")
                ),
                dynamicTest("저장된 모든 테마를 조회한다.", () -> {
                            jdbcTemplate.update(
                                    "INSERT INTO theme (name, description, thumbnail) VALUES ('방탈출2', '2번 방탈출', '썸네일2')");

                            RestAssured.given().log().all()
                                    .contentType(ContentType.JSON)
                                    .when().get("/themes")
                                    .then().log().all()
                                    .statusCode(200)
                                    .body("size()", is(2));
                        }
                ),
                dynamicTest("시간을 삭제한다.", () ->
                        RestAssured.given().log().all()
                                .when().delete("/themes/1")
                                .then().log().all()
                                .statusCode(204)
                ),
                dynamicTest("존재하지 않는 시간을 삭제하려고 시도하면 Bad Request status를 반환한다.", () ->
                        RestAssured.given().log().all()
                                .when().delete("/themes/1")
                                .then().log().all()
                                .statusCode(400)
                ));
    }

    @Nested
    @DisplayName("인기 테마 조회 테스트")
    class PopularThemes {

        @MockBean
        private Clock clock;

        @Test
        @Sql(value = "classpath:popular_data.sql")
        @DisplayName("인기 테마를 조회한다.")
        void readPopularThemes() {
            given(clock.instant()).willReturn(Instant.parse("2024-05-10T00:00:00Z"));
            given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));

            RestAssured.given().log().all()
                    .when().get("themes/populars")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(10));
        }
    }
}
