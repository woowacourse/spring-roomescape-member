package roomescape.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ThemeIntegrationTest extends IntegrationTest {
    @Test
    void 테마_목록을_조회할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Disabled
    @Test
    void 최근_일주일동안_예약_건수_많은_순서대로_10개_테마를_인기_테마로_조회할_수_있다() {
        for (int date = 1; date < 10; date++) {
            jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                    "브라운", "1999-09-0" + date, "1", "1");
        }
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "브라운", "2024-08-10", "1", "1");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "브라운", "2024-08-11", "1", "1"); // TODO: 반복문으로 해결하기...

        RestAssured.given().log().all()
                .when().get("/themes/ranking")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10)); // TODO: body 검증
    }

    @Test
    void 테마를_추가할_수_있다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨3");
        params.put("description", "내용이다.");
        params.put("thumbnail", "https://naver.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/themes/2")
                .body("id", is(2));
    }

    @Test
    void 필드가_빈_값이면_테마를_추가할_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨3");
        params.put("description", null);
        params.put("thumbnail", "https://naver.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 테마를_삭제할_수_있다() {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", 1);

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 존재하지_않는_테마는_삭제할_수_없다() {
        RestAssured.given().log().all()
                .when().delete("/themes/13")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 예약이_존재하는_테마는_삭제할_수_없다() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400);
    }
}
