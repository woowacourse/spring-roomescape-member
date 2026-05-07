package roomescape.web;


import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.time.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserReservationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void 예약_가능한_시간_목록_조회() {
        createReservationTime("10:00");
        createReservationTime("11:00");
        createReservationTime("12:00");
        createReservationTime("13:00");
        createTheme("우아한 테마", "우아한테크코스 전용 테마입니다.", "https://example.com/image.png");
        createTheme("페어 테마", "페어 전용 테마입니다.", "https://example.com/pair.png");

        List<ReservationTime> beforeReservationResults = getAvailableTimes(LocalDate.of(2026, 5, 1), 1L);

        assertThat(beforeReservationResults).hasSize(4);
        assertThat(beforeReservationResults.stream().map(ReservationTime::getId).toList())
                .containsExactly(1L, 2L, 3L, 4L);
        assertThat(beforeReservationResults.stream().map(ReservationTime::getStartAt).toList())
                .containsExactly(
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0)
        );

        createReservation("브라운", LocalDate.of(2026, 5, 1), 1L, 1L);
        createReservation("포비", LocalDate.of(2026, 5, 2), 2L, 2L);

        assertThat(getAvailableTimes(LocalDate.of(2026, 5, 1), 1L)).hasSize(3);
        assertThat(getAvailableTimes(LocalDate.of(2026, 5, 2), 1L)).hasSize(4);
        assertThat(getAvailableTimes(LocalDate.of(2026, 5, 1), 2L)).hasSize(4);
        assertThat(getAvailableTimes(LocalDate.of(2026, 5, 2), 2L)).hasSize(3);
    }

    private void createReservationTime(String startAt) {
        Map<String, Object> reservationTime = new HashMap<>();
        reservationTime.put("startAt", startAt);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/admin/times")
                .then().statusCode(201);
    }

    private void createTheme(String name, String description, String thumbnailUrl) {
        Map<String, Object> theme = new HashMap<>();
        theme.put("name", name);
        theme.put("description", description);
        theme.put("thumbnailUrl", thumbnailUrl);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().statusCode(201);
    }

    private List<ReservationTime> getAvailableTimes(LocalDate date, Long themeId) {
        return RestAssured.given()
                .queryParam("date", date.toString())
                .queryParam("themeId", themeId)
                .when().get("/times/available-times")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReservationTime.class);
    }


    private void createReservation(String name, LocalDate date, Long timeId, Long themeId) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("date", date.toString());
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().statusCode(201);
    }
}
