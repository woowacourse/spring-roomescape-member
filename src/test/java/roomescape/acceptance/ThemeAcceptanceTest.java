package roomescape.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void GET_themes_목록을_조회한다() {
        insertTheme(1L, "공포");

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("themes.size()", is(1));
    }

    @Test
    void GET_themes_id_단건을_조회한다() {
        insertTheme(1L, "공포");

        RestAssured.given().log().all()
                .when().get("/themes/1")
                .then().log().all()
                .statusCode(200)
                .body("name", equalTo("공포"));
    }

    @Test
    void GET_themes_id_times_예약된_시간은_isReserved_true_나머지는_false() {
        insertTheme(1L, "공포");
        insertTime(1L, "10:00");
        insertTime(2L, "11:00");
        insertReservation("브라운", 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .when().get("/themes/1/times?date=2026-05-06")
                .then().log().all()
                .statusCode(200)
                .body("times.find { it.id == 1 }.isReserved", is(true))
                .body("times.find { it.id == 2 }.isReserved", is(false));
    }

    @Test
    void GET_themes_popular_인기_테마_목록을_조회한다() {
        insertTheme(1L, "1위");
        insertTime(1L, "10:00");
        insertReservation("a", 1L, "2026-05-01", 1L);

        RestAssured.given().log().all()
                .when().get("/themes/popular?limit=10")
                .then().log().all()
                .statusCode(200)
                .body("themes.size()", is(1))
                .body("themes[0].id", is(1))
                .body("themes[0].reservedCount", is(1));
    }

    private void insertTheme(Long id, String name) {
        jdbcTemplate.update(
                "INSERT INTO theme(id, name, description, thumbnail_image_url) VALUES (?, ?, '설명', 'https://thumbnail.url')",
                id, name);
    }

    private void insertTime(Long id, String startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time(id, start_at) VALUES (?, ?)", id, startAt);
    }

    private void insertReservation(String name, Long themeId, String date, Long timeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation(name, theme_id, date, time_id) VALUES (?, ?, ?, ?)",
                name, themeId, date, timeId);
    }
}