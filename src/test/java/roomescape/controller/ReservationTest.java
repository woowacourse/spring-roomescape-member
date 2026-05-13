package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReservationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    @DisplayName("예약 생성 테스트")
    void createReservation() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (2, '우주 탐험대', 'https://picsum.photos/seed/space/400/300', '은하계를 누비는 우주 탐험', 'AVAILABLE')
            """);

        LocalDate reservedDate = LocalDate.now().plusDays(1);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "녀녕");
        params.put("date", reservedDate);
        params.put("timeId", 1L);
        params.put("themeId", 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("녀녕"))
                .body("[0].date", is(reservedDate.toString()))
                .body("[0].time.id", is(1))
                .body("[0].time.startAt", is("10:00"))
                .body("[0].theme.id", is(2))
                .body("[0].theme.name", is("우주 탐험대"))
                .body("[0].theme.thumbnailUrl", is("https://picsum.photos/seed/space/400/300"))
                .body("[0].theme.description", is("은하계를 누비는 우주 탐험"));
    }

    @Test
    @DisplayName("예약 삭제 테스트")
    void deleteReservationTest() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (1, '공포의 저택', 'url1', '설명1', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO reservation
            VALUES (1, 'user_a', '2026-04-28', 'AVAILABLE', 1, 1)
            """);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("지난 날짜,시간에 대한 예약 시 400 상태를 반환해야한다.")
    void pastReservationRequestTest() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time
            VALUES (1, '10:00', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO theme
            VALUES (1, '공포의 저택', 'url1', '설명1', 'AVAILABLE')
            """);
        jdbcTemplate.update("""
            INSERT INTO reservation
            VALUES (1, 'user_a', '2026-04-28', 'AVAILABLE', 1, 1)
            """);

        LocalDate pastDate = LocalDate.now().minusDays(1);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "녀녕");
        params.put("date", pastDate);
        params.put("timeId", 1L);
        params.put("themeId", 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is(ErrorMessage.CANNOT_SELECT_PAST_DATETIME.getMessage()));
    }
}
