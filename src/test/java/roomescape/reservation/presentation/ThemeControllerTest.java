package roomescape.reservation.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"/truncate.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

        // CASCADE 해결용
        jdbcTemplate.update("DELETE FROM schedule");
        jdbcTemplate.update("DELETE FROM reservation");

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
    void 각_날짜에_존재하는_모든_테마_조회_API_테스트() {
        RestAssured.given().log().all()
                .queryParam("date", "2026-05-05")
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10));
    }

    @Test
    void 최근_7일_예약_개수에_따른_인기_테마_조회_API_테스트() {
        RestAssured.given().log().all()
                .queryParam("day", 7)
                .queryParam("limit", 10)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
                .body("[0].id", is(3))
                .body("[1].id", is(2))
                .body("[2].id", is(1));
    }
}
