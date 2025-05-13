package roomescape.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql({"/schema.sql", "/data.sql"})
public class ThemeIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = this.port;
    }

    @DisplayName("테마를 추가한다.")
    @Test
    void addThemeTest() {
        final Map<String, String> params = new HashMap<>();
        params.put("name", "test");
        params.put("description", "test");
        params.put("thumbnail", "test");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마 목록을 조회한다.")
    @Test
    void findAllTest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("최근 일주일 인기 테마 목록을 가져온다.")
    @Test
    void findPopularThemesInRecentSevenDaysTest() {
        final LocalDate beforeOneDay = LocalDate.now().minusDays(1);
        final LocalDate beforeEightDay = LocalDate.now().minusDays(8);
        final String themeSql = "INSERT INTO theme (name, description, thumbnail) VALUES ('test', 'test', 'test')";
        final String reservationTimeSql = "INSERT INTO reservation_time (start_at) VALUES ('10:10')";
        final String reservationSql = "INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(themeSql);
        jdbcTemplate.update(reservationTimeSql);
        jdbcTemplate.update(reservationSql, beforeOneDay.toString(), 1L, 1L, 1L);
        jdbcTemplate.update(reservationSql, beforeEightDay.toString(), 1L, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void deleteTest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/3")
                .then().log().all()
                .statusCode(204);
    }
}
