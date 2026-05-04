package roomescape.reservationtime.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("""
                INSERT INTO theme (name, description, thumbnail_img_url)
                VALUES (?, ?, ?)
                """, "theme name", "theme description", "theme img url");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "12:00");
        jdbcTemplate.update("""
                INSERT INTO reservation (name, date, theme_id, time_id)
                VALUES (?, ?, ?, ?)
                """, "예약자", "2026-05-04", 1L, 1L);
    }

    @DisplayName("특정 날짜/테마의 예약 가능 시간대 조회 API를 테스트합니다.")
    @Test
    void find_available_times() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("date", "2026-05-04")
                .queryParam("themeId", "1")
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("time", containsInAnyOrder("11:00", "12:00"));
    }
}
