package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.service.dto.response.ReservationTimeStatusResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void 날짜와_테마를_선택해_예약가능한_시간_조회_API() {
        // 시간 추가
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "10:00", "10:30");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "11:00", "11:30");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "12:00", "12:30");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "13:00", "13:30");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "14:00", "14:30");

        // 테마 추가
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)", "링", "공포 테마", "http:~");

        // 예약 전
        List<ReservationTimeStatusResponse> timeStatusesBeforeReservation = getReservationTimeStatusResponses();
        assertThat(timeStatusesBeforeReservation.size()).isEqualTo(5);
        assertThat(countReservableTimes(timeStatusesBeforeReservation)).isEqualTo(5);

        // 예약 추가 1
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "브라운", "2026-05-05", "1", "1");

        // 예약 후
        List<ReservationTimeStatusResponse> timeStatusesAfterReservation = getReservationTimeStatusResponses();
        assertThat(timeStatusesAfterReservation.size()).isEqualTo(5);
        assertThat(countReservableTimes(timeStatusesAfterReservation)).isEqualTo(4);
    }

    private static List<ReservationTimeStatusResponse> getReservationTimeStatusResponses() {
        return RestAssured.given().log().all()
                .when().get("/times?date=2026-05-05&themeId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTimeStatusResponse.class);
    }

    private static int countReservableTimes(final List<ReservationTimeStatusResponse> timeStatuses) {
        int count = 0;
        for (final ReservationTimeStatusResponse timeStatus : timeStatuses) {
            if (!timeStatus.reserved()) {
                count++;
            }
        }
        return count;
    }
}
