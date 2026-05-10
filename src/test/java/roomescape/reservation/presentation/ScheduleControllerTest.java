package roomescape.reservation.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"/truncate.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ScheduleControllerTest {

    @Test
    void 스케줄_생성() {
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("date", "2026-05-06");
        schedule.put("timeId", 1);
        schedule.put("themeId", 4);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(schedule)
                .when().post("/schedule")
                .then().log().all()
                .statusCode(201)
                .body("id", is(6))
                .body("date", is("2026-05-06"))
                .body("time_id", is(1))
                .body("theme_id", is(4));
    }

    @Test
    void 스케줄_조회() {
        RestAssured.given().log().all()
                .when().get("/schedule/1")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("date", is("2026-05-05"))
                .body("time_id", is(1))
                .body("theme_id", is(1));
    }

    @Test
    void 스케줄_삭제() {
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("date", "2026-05-06");
        schedule.put("timeId", 1);
        schedule.put("themeId", 4);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(schedule)
                .when().post("/schedule")
                .then().log().all()
                .statusCode(201)
                .body("id", is(6));

        RestAssured.given().log().all()
                .when().delete("/schedule/6")
                .then().log().all()
                .statusCode(204);
    }
}
