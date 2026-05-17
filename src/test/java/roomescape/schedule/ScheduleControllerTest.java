package roomescape.schedule;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.config.TestTimeConfig;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@Import(TestTimeConfig.class)
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
                .when().post("/schedules")
                .then().log().all()
                .statusCode(201)
                .body("success", is(true))
                .body("data.id", is(6))
                .body("data.date", is("2026-05-06"))
                .body("data.time_id", is(1))
                .body("data.theme_id", is(4));
    }

    @Test
    void 스케줄_조회() {
        RestAssured.given().log().all()
                .when().get("/schedules/1")
                .then().log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(1))
                .body("data.date", is("2026-05-05"))
                .body("data.time_id", is(1))
                .body("data.theme_id", is(1));
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
                .when().post("/schedules")
                .then().log().all()
                .statusCode(201)
                .body("success", is(true))
                .body("data.id", is(6));

        RestAssured.given().log().all()
                .when().delete("/schedules/6")
                .then().log().all()
                .statusCode(204);
    }
}
