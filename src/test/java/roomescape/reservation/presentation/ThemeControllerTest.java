package roomescape.reservation.presentation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ThemeControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 테마_저장_API_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "무서운게 딱 좋아");
        params.put("description", "무서운 분위기의 방탈출");
        params.put("thumbnailUrl", "https://example.com/theme.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 테마_추가_및_삭제_API_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "무서운게 딱 좋아");
        params.put("description", "무서운 분위기의 방탈출");
        params.put("thumbnailUrl", "https://example.com/theme.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 각_날짜에_존재하는_모든_테마_조회_API_테스트(){
        Map<String, String> firstTheme = new HashMap<>();
        firstTheme.put("name", "꿀잼 방탈출");
        firstTheme.put("description", "재밌는 분위기의 방탈출");
        firstTheme.put("thumbnailUrl", "https://example.com/theme_happy.jpg");

        Map<String, String> secondTheme = new HashMap<>();
        secondTheme.put("name", "무서운게 딱 좋아");
        secondTheme.put("description", "무서운 분위기의 방탈출");
        secondTheme.put("thumbnailUrl", "https://example.com/theme.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(firstTheme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(secondTheme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2));

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "16:00");

        jdbcTemplate.update("INSERT INTO schedule (date, time_id, theme_id) VALUES (?, ?, ?)", "2026-05-04", 1L, 1L);
        jdbcTemplate.update("INSERT INTO schedule (date, time_id, theme_id) VALUES (?, ?, ?)", "2026-05-04", 2L, 1L);
        jdbcTemplate.update("INSERT INTO schedule (date, time_id, theme_id) VALUES (?, ?, ?)", "2026-05-04", 1L, 2L);
        jdbcTemplate.update("INSERT INTO schedule (date, time_id, theme_id) VALUES (?, ?, ?)", "2026-05-05", 2L, 2L);

        RestAssured.given().log().all()
                .queryParam("date", "2026-05-04")
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].name", is("꿀잼 방탈출"))
                .body("[1].id", is(2))
                .body("[1].name", is("무서운게 딱 좋아"));
    }

}
