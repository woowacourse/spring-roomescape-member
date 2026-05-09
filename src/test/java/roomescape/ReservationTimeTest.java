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
                .body("size()", is(2));
    }
}
