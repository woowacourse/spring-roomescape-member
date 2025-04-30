package roomescape.presentation.controller.Integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_2;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_3;
import static roomescape.testFixture.Fixture.THEME_1;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.repository.dto.AvailableTimesData;
import roomescape.testFixture.JdbcHelper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserReservationControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        RestAssured.port = port;

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("테마와 날짜 선택 후 예약 가능한 시간 조회 요청")
    @Test
    void getAllThemes() {
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertReservationTimes(jdbcTemplate, RESERVATION_TIME_1, RESERVATION_TIME_2, RESERVATION_TIME_3);
        JdbcHelper.insertReservation(jdbcTemplate, RESERVATION_1);

        String date = RESERVATION_1.getReservationDate().toString();

        List<AvailableTimesData> timesData = RestAssured.given().log().all()
                .when().get("/reservation/themes/1/available-times?date=" + date)
                .then().log().all()
                .statusCode(200)
                .extract()
                .body().jsonPath().getList(".", AvailableTimesData.class);

        long bookedCount = timesData.stream()
                .filter(AvailableTimesData::alreadyBooked)
                .count();

        assertAll(
                () -> assertThat(timesData).hasSize(3),
                () -> assertThat(bookedCount).isEqualTo(1)
        );
    }
}
