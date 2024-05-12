package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Theme;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @LocalServerPort
    int serverPort;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = serverPort;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("DB에서 테마 목록 조회 API 작동을 확인한다")
    void checkAllThemes() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마명",
                "테마 설명",
                "테마 이미지"
        );

        List<Theme> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Theme.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(*) from theme", Integer.class);

        assertThat(themes.size()).isEqualTo(count);
    }

    @TestFactory
    @DisplayName("DB에서 테마 추가와 삭제의 작동을 확인한다")
    Stream<DynamicTest> themeCreateAndDelete() {
        Map<String, String> params = Map.of(
                "name", "테마명",
                "description", "테마 설명",
                "thumbnail", "테마 이미지"
        );

        return Stream.of(
                dynamicTest("테마를 추가한다", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params)
                            .when().post("/themes")
                            .then().log().all()
                            .statusCode(201);
                }),

                dynamicTest("테마가 정상적으로 추가되었는지 확인한다", () -> {
                    Integer count = jdbcTemplate.queryForObject("SELECT count(*) from theme", Integer.class);
                    assertThat(count).isEqualTo(1);
                }),

                dynamicTest("id가 1인 테마를 삭제한다", () -> {
                    RestAssured.given().log().all()
                            .when().delete("/themes/1")
                            .then().log().all()
                            .statusCode(204);
                }),

                dynamicTest("테마가 정상적으로 삭제되었는지 확인한다", () -> {
                    Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(*) from theme", Integer.class);
                    assertThat(countAfterDelete).isEqualTo(0);
                })
        );
    }

    @TestFactory
    @DisplayName("중복된 테마 추가가 불가능한지 확인한다.")
    Stream<DynamicTest> checkDuplicatedTheme() {
        Map<String, String> params1 = Map.of(
                "name", "테마명",
                "description", "테마 설명1",
                "thumbnail", "테마 이미지1"
        );

        Map<String, String> params2 = Map.of(
                "name", "테마명",
                "description", "테마 설명2",
                "thumbnail", "테마 이미지2"
        );

        return Stream.of(
                dynamicTest("테마를 추가한다", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params1)
                            .when().post("/themes")
                            .then().log().all()
                            .statusCode(201);
                }),

                dynamicTest("중복된 테마를 추가한다", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params2)
                            .when().post("/themes")
                            .then().log().all()
                            .statusCode(400);
                })
        );
    }

    @Test
    @DisplayName("존재하지 않는 테마의 삭제가 불가능한지 확인한다")
    void checkNotExistThemeDelete() {
        RestAssured.given().log().all()
                .when().delete("themes/1")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("주간 인기 테마 목록 조회 API 작동을 확인한다")
    void checkWeeklyHotThemes() {
        RestAssured.given().log().all()
                .when().get("themes/hot/weekly")
                .then().log().all()
                .statusCode(200);
    }
}
