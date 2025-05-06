package roomescape.time.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.testFixture.Fixture.MEMBER_1;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_2;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_3;
import static roomescape.testFixture.Fixture.THEME_1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.time.domain.ReservationTime;
import roomescape.reservation.domain.repository.dto.TimeDataWithBookingInfo;
import roomescape.testFixture.JdbcHelper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimeControllerTest {
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
        jdbcTemplate.execute("TRUNCATE TABLE members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }


    @DisplayName("모든 time 조회 api")
    @Test
    void getAllTimesApiTest() {
        JdbcHelper.insertReservationTimes(jdbcTemplate, RESERVATION_TIME_1, RESERVATION_TIME_2);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("시간 추가 api")
    @Test
    void createTimeApiTest() {

        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("id로 time 삭제 api")
    @Test
    void deleteTime() {
        ReservationTime reservationTime = RESERVATION_TIME_1;
        JdbcHelper.insertReservationTime(jdbcTemplate, reservationTime);

        Long id = reservationTime.getId();
        RestAssured.given().log().all()
                .when().delete("/times/" + id)
                .then().log().all()
                .statusCode(204);

        assertThat(getTimesCount()).isEqualTo(0);
    }

    @DisplayName("예약이 존재하는 시간은 삭제 불가")
    @Test
    void cannotDeleteTime_when_hasReservation() {
        ReservationTime reservationTime = RESERVATION_TIME_1;
        JdbcHelper.insertReservationTime(jdbcTemplate, reservationTime);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertMember(jdbcTemplate, MEMBER_1);
        JdbcHelper.insertReservationOnly(jdbcTemplate, RESERVATION_1);

        Long timeId = reservationTime.getId();
        RestAssured.given().log().all()
                .when().delete("/times/" + timeId)
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("테마와 날짜 선택 후 예약 가능한 시간 조회 요청")
    @Test
    void getTimesWithBookingInfo() {
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertReservationTimes(jdbcTemplate, RESERVATION_TIME_1, RESERVATION_TIME_2, RESERVATION_TIME_3);
        JdbcHelper.insertMember(jdbcTemplate, MEMBER_1);
        JdbcHelper.insertReservationOnly(jdbcTemplate, RESERVATION_1);

        String date = RESERVATION_1.getReservationDate().toString();

        List<TimeDataWithBookingInfo> timesData = RestAssured.given().log().all()
                .when().get(String.format("/times/booking-status?date=%s&themeId=%d", date, THEME_1.getId()))
                .then().log().all()
                .statusCode(200)
                .extract()
                .body().jsonPath().getList(".", TimeDataWithBookingInfo.class);

        long bookedCount = timesData.stream()
                .filter(TimeDataWithBookingInfo::alreadyBooked)
                .count();

        assertAll(
                () -> assertThat(timesData).hasSize(3),
                () -> assertThat(bookedCount).isEqualTo(1)
        );
    }

    private int getTimesCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);
    }
}
