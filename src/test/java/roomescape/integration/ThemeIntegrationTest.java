package roomescape.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "spring.sql.init.mode=always",
                "spring.sql.init.data-locations=classpath:/test-data.sql"
        })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @DisplayName("테마를 추가한다.")
    @Test
    void addThemeTest() {
        Map<String, String> params = new HashMap<>();
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
        Map<String, String> params = new HashMap<>();
        params.put("name", "test");
        params.put("description", "test");
        params.put("thumbnail", "test");

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
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("최근 일주일 인기 테마 목록을 가져온다.")
    @Test
    void findPopularThemesInRecentSevenDaysTest() {
        LocalDate beforeOneDay = LocalDate.now().minusDays(1);
        LocalDate beforeEightDay = LocalDate.now().minusDays(8);
        String themeSql = "INSERT INTO theme (name, description, thumbnail) VALUES ('test', 'test', 'test')";
        String reservationTimeSql = "INSERT INTO reservation_time (start_at) VALUES ('10:10')";
        String reservationSql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('test', ?, ?, ?)";
        jdbcTemplate.update(themeSql);
        jdbcTemplate.update(reservationTimeSql);
        jdbcTemplate.update(reservationSql, beforeOneDay.toString(), 1L, 1L);
        jdbcTemplate.update(reservationSql, beforeEightDay.toString(), 1L, 1L);

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
        Map<String, String> params = new HashMap<>();
        params.put("name", "test");
        params.put("description", "test");
        params.put("thumbnail", "test");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }
}
