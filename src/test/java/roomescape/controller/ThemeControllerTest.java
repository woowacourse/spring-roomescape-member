package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.request.ThemeRequest;
import roomescape.controller.response.ThemeResponse;
import roomescape.model.Theme;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert reservationInsertActor;
    private SimpleJdbcInsert timeInsertActor;
    private SimpleJdbcInsert themeInsertActor;

    @BeforeEach
    void setUp() {
        reservationInsertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        timeInsertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        themeInsertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @DisplayName("전체 테마를 조회한다.")
    @Test
    void should_get_themes() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "에버", "공포", "공포.jpg");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "배키", "미스터리", "미스터리.jpg");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "포비", "스릴러", "스릴러.jpg");

        List<ThemeResponse> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ThemeResponse.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);
        assertThat(themes).hasSize(count);
    }

    @DisplayName("테마를 추가한다.")
    @Test
    void should_add_theme() {
        ThemeRequest request = new ThemeRequest("에버", "공포", "공포.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/themes/3");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);

        assertThat(count).isEqualTo(3);
    }

    @DisplayName("테마를 삭제한다")
    @Test
    void should_remove_theme() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "에버", "공포", "공포.jpg");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "배키", "미스터리", "미스터리.jpg");

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);
        assertThat(count).isEqualTo(3);
    }

    @DisplayName("인기 테마를 조회한다.")
    @Test
    void should_find_popular_theme() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE theme RESTART IDENTITY");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
        insertReservationTime(LocalTime.of(10, 0));
        for (int i = 1; i <= 15; i++) {
            insertTheme("name" + i, "description" + i, "thumbnail" + i);
        }
        for (int i = 1; i <= 10; i++) {
            insertReservation("name" + i, LocalDate.now().minusDays(i % 7 + 1), 1L, i);
        }
        insertReservation("name11", LocalDate.now().minusDays(1), 1L, 10);
        insertReservation("name12", LocalDate.now().minusDays(1), 1L, 10);
        insertReservation("name13", LocalDate.now().minusDays(1), 1L, 10);
        insertReservation("name14", LocalDate.now().minusDays(1), 1L, 9);
        insertReservation("name15", LocalDate.now().minusDays(1), 1L, 9);

        jdbcTemplate.queryForObject("select count(1) from reservation", Long.class);

        List<ThemeResponse> popularThemes = RestAssured.given().log().all()
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", ThemeResponse.class);

        assertThat(popularThemes).hasSize(10);
        assertAll(() -> {
            assertThat(popularThemes.get(0).getId()).isEqualTo(10);
            assertThat(popularThemes.get(1).getId()).isEqualTo(9);
            assertThat(popularThemes.get(2).getId()).isEqualTo(1);
            assertThat(popularThemes.get(3).getId()).isEqualTo(2);
            assertThat(popularThemes.get(4).getId()).isEqualTo(3);
            assertThat(popularThemes.get(5).getId()).isEqualTo(4);
            assertThat(popularThemes.get(6).getId()).isEqualTo(5);
            assertThat(popularThemes.get(7).getId()).isEqualTo(6);
            assertThat(popularThemes.get(8).getId()).isEqualTo(7);
            assertThat(popularThemes.get(9).getId()).isEqualTo(8);
        });
    }

    private void insertReservationTime(LocalTime startAt) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("start_at", startAt);
        timeInsertActor.execute(parameters);
    }

    private void insertReservation(String name, LocalDate date, long timeId, long themeId) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("name", name);
        parameters.put("date", date);
        parameters.put("time_id", timeId);
        parameters.put("theme_id", themeId);
        reservationInsertActor.execute(parameters);
    }

    private void insertTheme(String name, String description, String thumbnail) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", name);
        parameters.put("description", description);
        parameters.put("thumbnail", thumbnail);
        themeInsertActor.execute(parameters);
    }
}
