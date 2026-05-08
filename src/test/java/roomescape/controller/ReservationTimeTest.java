package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
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
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("""
            DELETE FROM reservation
            """);
        jdbcTemplate.update("""
            DELETE FROM reservation_time
            """);
        jdbcTemplate.update("""
            DELETE FROM theme
            """);
    }

    @Test
    @DisplayName("예약이 없는 경우 테스트")
    void readAvailableTime() {

        RestAssured.given().log().all()
                .queryParam("date", "2027-05-03")
                .queryParam("themeId", 7L)
                .when().get("/times")
                .then()
                .statusCode(200).log().all()
                .body("size()", is(2));;
    }

    @Test
    @DisplayName("예약이 존재하는 경우 테스트")
    void readAvailableTimeWithExistReservation() {

        RestAssured.given().log().all()
                .queryParam("date", "2026-05-03")
                .queryParam("themeId", 7L)
                .when().get("/times")
                .then()
                .statusCode(200).log().all()
                .body("size()", is(1));;
    }
}
