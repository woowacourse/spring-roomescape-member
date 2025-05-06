package roomescape;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeApiTest {

    private static final String FUTURE_DATE_TEXT = LocalDate.now().plusDays(1).toString();
    private static final String PAST_DATE_TEXT = LocalDate.now().minusDays(1).toString();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");

        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("테마를 추가할 수 있다.")
    @Test
    void canCreateTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "이름");
        params.put("description", "설명");
        params.put("thumbnail", "썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("location", "theme/1");
    }

    @DisplayName("유효하지 않은 요청으로는 테마를 추가할 수 없다.")
    @Test
    void cannotCreateThemeWhenInvalidRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "이름");
        params.put("description", "");
        params.put("thumbnail", "썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("모든 테마를 조회할 수 있다")
    @Test
    void canResponseAllTheme() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명",
                "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명",
                "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명",
                "썸네일");

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("모든 테마를 조회할 수 있다")
    @Test
    void canResponseTopThemes() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "인기테마", "설명1",
                "썸네일1");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "평범테마", "설명2",
                "썸네일2");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "인기없는테마", "설명3",
                "썸네일3");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마1", "설명1",
                "썸네일1");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마2", "설명2",
                "썸네일2");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마3", "설명3",
                "썸네일3");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마4", "설명1",
                "썸네일1");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마5", "설명2",
                "썸네일2");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마6", "설명3",
                "썸네일3");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마7", "설명1",
                "썸네일1");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마8", "설명2",
                "썸네일2");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마9", "설명3",
                "썸네일3");

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "12:00");

        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                PAST_DATE_TEXT, 1, 1);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                PAST_DATE_TEXT, 2, 1);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                PAST_DATE_TEXT, 3, 1);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                PAST_DATE_TEXT, 1, 2);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                PAST_DATE_TEXT, 2, 2);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                PAST_DATE_TEXT, 1, 3);

        RestAssured.given().log().all()
                .when().get("/themes/top")
                .then().log().all()
                .statusCode(200)
                .body("get(0).name", is("인기테마"))
                .body("get(1).name", is("평범테마"))
                .body("get(2).name", is("인기없는테마"))
                .body("size()", is(10));
    }

    @DisplayName("ID를 통해 테마를 삭제할 수 있다")
    @Test
    void canDeleteThemeById() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명",
                "썸네일");

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 테마를 삭제할 수 없다")
    @Test
    void cannotDeleteThemeByIdWhenNotExist() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명",
                "썸네일");

        RestAssured.given().log().all()
                .when().delete("/themes/2")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("이미 테마에 대한 예약이 존재한다면 해당 테마의 삭제가 불가능하다.")
    @Test
    void cannotDeleteThemeByIdWhenReservationExist() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)", "이름1", "설명1",
                "썸네일1");
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                FUTURE_DATE_TEXT, "1", "1");

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400);
    }
}
