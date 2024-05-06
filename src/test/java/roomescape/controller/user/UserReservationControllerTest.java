package roomescape.controller.user;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.LocalDateFixture.*;

import io.restassured.RestAssured;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserReservationControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into reservation_time values(1,'10:00')");
        jdbcTemplate.update("insert into theme values(1,'리비', '리비 설명', 'url')");
        LocalDate reservationDate = AFTER_TWO_DAYS_DATE;
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values(?,?,?,?)", "브라운",
                reservationDate, 1, 1);
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.update("delete from reservation");
    }

    @DisplayName("예약 가능 시각 목록을 불러올 수 있다. (200 OK)")
    @Test
    void should_response_bookable_time() {
        RestAssured.given().log().all()
                .when().get("/bookable-times?date="+ AFTER_TWO_DAYS_DATE+"&themeId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
