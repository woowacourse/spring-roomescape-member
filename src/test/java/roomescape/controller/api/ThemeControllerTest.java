package roomescape.controller.api;

import static org.hamcrest.Matchers.is;
import static roomescape.util.Fixture.THEME_DESCRIPTION;
import static roomescape.util.Fixture.THEME_NAME;
import static roomescape.util.Fixture.THEME_THUMBNAIL;
import static roomescape.util.Fixture.YESTERDAY;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class ThemeControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @ParameterizedTest
    @DisplayName("테마 생성 시, name 값이 null이거나 비어있으면 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void validateThemeWithNameEmpty(final String name) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("description", THEME_DESCRIPTION);
        params.put("thumbnail", THEME_THUMBNAIL);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @ParameterizedTest
    @DisplayName("테마 생성 시, description 값이 null이거나 비어있으면 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void validateThemeWithDescriptionEmpty(final String description) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", THEME_NAME);
        params.put("description", description);
        params.put("thumbnail", THEME_THUMBNAIL);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @ParameterizedTest
    @DisplayName("테마 생성 시, thumbnail 값이 null이거나 비어있으면 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void validateThemeWithThumbnailEmpty(final String thumbnail) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", THEME_NAME);
        params.put("description", THEME_DESCRIPTION);
        params.put("thumbnail", thumbnail);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("테마 생성 시, name 값이 중복이면 예외가 발생한다.")
    void validateThemeWithDuplicatedName() {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", THEME_NAME);
        params.put("description", THEME_DESCRIPTION);
        params.put("thumbnail", THEME_THUMBNAIL);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("모든 테마 목록을 조회한다.")
    void findAllThemes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(countTheme()));
    }

    @Test
    @DisplayName("지난 한 주 동안의 인기 테마 목록을 조회한다.")
    void findPopularThemesInLastWeek() {
        insertReservation(YESTERDAY, 1, 1, 1);

        RestAssured.given().log().all()
                .when().get("/themes/popular?period-day=7")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("테마 삭제 시, 해당 테마를 참조하는 예약이 있으면 예외가 발생한다.")
    void validateThemeDelete() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("테마 삭제 시, 해당 테마를 참조하는 예약이 없으면 테마가 삭제된다.")
    void deleteTheme() {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", THEME_NAME);
        params.put("description", THEME_DESCRIPTION);
        params.put("thumbnail", THEME_THUMBNAIL);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/themes/" + findLastIdOfTheme())
                .then().log().all()
                .statusCode(204);
    }

    private int countTheme() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM theme", Integer.class);
    }

    private void insertReservation(final String date, final int timeId, final int themeId, final int memberId) {
        final String sql = "INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, date, timeId, themeId, memberId);
    }

    private int findLastIdOfTheme() {
        return jdbcTemplate.queryForObject("SELECT max(id) FROM theme", Integer.class);
    }
}
