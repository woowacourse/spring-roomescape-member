package roomescape.controller.user;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static roomescape.fixture.LocalDateFixture.AFTER_TWO_DAYS_DATE;

import io.restassured.RestAssured;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserThemeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into reservation_time values(1,'10:00')");
        jdbcTemplate.update("insert into theme values(1,'리비', '리비 설명', 'url')");
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values(?,?,?,?)", "브라운",
                AFTER_TWO_DAYS_DATE, 1, 1);
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.update("delete from reservation");
    }

    @DisplayName("인기 테마 목록을 불러올 수 있다.(200 OK)")
    @Test
    void should_response_theme_ranking() {
        RestAssured.given().log().all()
                .when().get("/theme-ranking")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
