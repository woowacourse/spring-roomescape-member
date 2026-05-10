package roomescape;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 시간_전체_조회() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('10:00')");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('11:00')");

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("times.size()", is(2));
    }

    @Test
    void 시간_단건_조회() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('10:00')");

        RestAssured.given().log().all()
                .when().get("/times/1")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("startAt", is("10:00"));
    }

    @Test
    void 시간_페이징_조회() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('10:00')");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('11:00')");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('12:00')");

        RestAssured.given().log().all()
                .when().get("/times?page=0&size=2")
                .then().log().all()
                .statusCode(200)
                .body("times.size()", is(2))
                .body("hasNext", is(true));

        RestAssured.given().log().all()
                .when().get("/times?page=1&size=2")
                .then().log().all()
                .statusCode(200)
                .body("times.size()", is(1))
                .body("hasNext", is(false));
    }
}
