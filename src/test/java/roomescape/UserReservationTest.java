package roomescape;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserReservationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");

        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    void 예약_가능한_시간_목록_조회() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"startAt\": \"10:00\"}")
                .when().post("/admin/times")
                .then().statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"startAt\": \"11:00\"}")
                .when().post("/admin/times")
                .then().statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"startAt\": \"12:00\"}")
                .when().post("/admin/times")
                .then().statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"startAt\": \"13:00\"}")
                .when().post("/admin/times")
                .then().statusCode(201);



        createTheme("우아한 테마", "우아한테크코스 전용 테마입니다.", "https://example.com/image.png");
        createTheme( "페어 테마", "페어 전용 테마입니다.", "https://example.com/pair.png");

        List<ReservationTime> beforeReservationResults = RestAssured.given().log().all()
                .when().get("/times/available-times?date=2026-05-01&themeId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(beforeReservationResults.size()).isEqualTo(4);

        createReservation("브라운", LocalDate.of(2026, 5, 1), 1L, 1L);
        createReservation("포비", LocalDate.of(2026, 5, 2), 2L, 2L);

        List<ReservationTime> afterReservationResults = RestAssured.given().log().all()
                .when().get("/times/available-times?date=2026-05-01&themeId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(afterReservationResults.size()).isEqualTo(3);

        List<ReservationTime> afterReservationResults_2 = RestAssured.given().log().all()
                .when().get("/times/available-times?date=2026-05-02&themeId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(afterReservationResults_2.size()).isEqualTo(4);

        List<ReservationTime> afterReservationResults_3 = RestAssured.given().log().all()
                .when().get("/times/available-times?date=2026-05-01&themeId=2")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(afterReservationResults_3.size()).isEqualTo(4);

        List<ReservationTime> afterReservationResults_4 = RestAssured.given().log().all()
                .when().get("/times/available-times?date=2026-05-02&themeId=2")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(afterReservationResults_4.size()).isEqualTo(3);
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
