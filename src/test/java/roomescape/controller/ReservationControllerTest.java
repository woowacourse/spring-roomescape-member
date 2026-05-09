package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.repository.dto.ReservationTimesWithStatus;
import roomescape.service.dto.response.ReservationOptionResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @TestConfiguration
    static class FixedClockConfig {

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(
                    LocalDate.of(2026, 5, 1)
                            .atStartOfDay(ZoneId.of("Asia/Seoul"))
                            .toInstant(),
                    ZoneId.of("Asia/Seoul")
            );
        }
    }

    @Test
    @Sql(scripts = {
            "/clear.sql",
            "/popular-themes-test-data.sql"
    })
    void 전체_날짜와_테마_조회() {
        ReservationOptionResponse responses = RestAssured.given().log().all()
                .when().get("/reservations/date-and-theme")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getObject(".", ReservationOptionResponse.class);

        // 기간 검증
        final LocalDate expectedStartDate = LocalDate.of(2026, 05, 01);
        final LocalDate expectedEndDate = expectedStartDate.plusDays(14 - 1);

        final List<LocalDate> actualDates = responses.dates();
        assertThat(actualDates).hasSize(14);

        assertThat(actualDates).doesNotContainAnyElementsOf(
                List.of(
                        expectedStartDate.minusDays(1),
                        expectedEndDate.plusDays(1)
                )
        );

        // 테마 검증
        assertThat(responses.themes()).hasSize(12);
    }

    @Test
    @Sql("/clear.sql")
    void 날짜와_테마를_선택해_예약가능한_시간_조회() {
        // 시간 추가
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "10:00", "10:30");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "11:00", "11:30");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "12:00", "12:30");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "13:00", "13:30");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "14:00", "14:30");

        // 테마 추가
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)", "링", "공포 테마", "http:~");

        // 예약 전
        List<ReservationTimesWithStatus> timeStatusesBeforeReservation = getReservationTimeStatusResponses();
        assertThat(timeStatusesBeforeReservation.size()).isEqualTo(5);
        assertThat(countReservableTimes(timeStatusesBeforeReservation)).isEqualTo(5);

        // 예약 추가 1
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "브라운", "2026-05-05", "1", "1");

        // 예약 후
        List<ReservationTimesWithStatus> timeStatusesAfterReservation = getReservationTimeStatusResponses();
        assertThat(timeStatusesAfterReservation.size()).isEqualTo(5);
        assertThat(countReservableTimes(timeStatusesAfterReservation)).isEqualTo(4);
    }

    @Test
    @Sql("/clear.sql")
    void 존재하지_않는_예약_시간으로_예약하면_404를_응답한다() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)", "링", "공포 테마", "http:~");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "브라운",
                        "date", "2026-05-05",
                        "timeId", 999,
                        "themeId", 1
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @Sql("/clear.sql")
    void 존재하지_않는_테마로_예약하면_404를_응답한다() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "10:00", "10:30");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "브라운",
                        "date", "2026-05-05",
                        "timeId", 1,
                        "themeId", 999
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @Sql("/clear.sql")
    void 예약자_이름이_비어있으면_400을_응답한다() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "10:00", "10:30");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)", "링", "공포 테마", "http:~");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "",
                        "date", "2026-05-05",
                        "timeId", 1,
                        "themeId", 1
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    private static List<ReservationTimesWithStatus> getReservationTimeStatusResponses() {
        return RestAssured.given().log().all()
                .when().get("/reservations/available-times?date=2026-05-05&themeId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTimesWithStatus.class);
    }

    private static int countReservableTimes(final List<ReservationTimesWithStatus> timeStatuses) {
        int count = 0;
        for (final ReservationTimesWithStatus timeStatus : timeStatuses) {
            if (!timeStatus.reserved()) {
                count++;
            }
        }
        return count;
    }
}
