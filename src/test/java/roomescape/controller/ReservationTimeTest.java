package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReservationTimeTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    @DisplayName("예약이 없는 날짜의 예약 가능 시간 조회 테스트")
    void readAvailableTime() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (2, '14:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (7, '좀비 연구소', 'url', '설명', 'AVAILABLE')
            """);

        RestAssured.given().log().all()
                .queryParam("date", "2027-05-03")
                .queryParam("themeId", 7L)
                .when().get("/times")
                .then()
                .statusCode(200).log().all()
                .body("size()", is(2));
    }

    @Test
    @DisplayName("예약이 존재하는 날짜의 예약 가능 시간 조회")
    void readAvailableTimeWithExistReservation() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (2, '14:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (7, '좀비 연구소', 'url', '설명', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO reservation
            VALUES (1, 'user_a', '2026-05-03', 'AVAILABLE', 1, 7)
            """);

        RestAssured.given().log().all()
                .queryParam("date", "2026-05-03")
                .queryParam("themeId", 7L)
                .when().get("/times")
                .then()
                .statusCode(200).log().all()
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약을 삭제하면 해당 날짜에 예약 시간이 복구되어야 한다.")
    void restoreAvailableTimeTest() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (2, '14:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (7, '좀비 연구소', 'url', '설명', 'AVAILABLE')
            """);

        jdbcTemplate.update("""
            INSERT INTO reservation
            VALUES (1, 'user_a', '2026-05-03', 'AVAILABLE', 1, 7)
            """);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("date", "2026-05-03")
                .queryParam("themeId", 7L)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].startAt", is("14:00"));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("date", "2026-05-03")
                .queryParam("themeId", 7L)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].startAt", is("10:00"))
                .body("[1].startAt", is("14:00"));
    }
}
