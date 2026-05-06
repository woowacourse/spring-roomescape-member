package roomescape.reservation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.domain.Reservation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        clearTables();
    }

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 예약_추가_및_삭제() {
        createTheme();
        createReservationTime("15:40", 1L);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void DB_조회_API_전환() {
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail_url) VALUES (?, ?, ?, ?)",
                1L, "미술관의 밤", "추리 테마", "https://example.com/theme.png");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at, theme_id) VALUES (?, ?, ?)", 1L, "15:40:00", 1L);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "브라운", "2023-08-05", 1L);

        List<Reservation> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
        assertThat(reservations.get(0).getTime().getId()).isEqualTo(1L);
    }

    @Test
    void DB_추가_삭제_API_전환() {
        createTheme();
        createReservationTime("10:00", 1L);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    void 예약과_시간_연결() {
        createTheme();
        createReservationTime("10:00", 1L);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 예약_가능_시간_조회() {
        createTheme();
        createReservationTime("10:00", 1L);
        createReservationTime("11:00", 1L);

        createReservation("브라운", "2026-05-10", 1L);

        RestAssured.given().log().all()
                .queryParam("date", "2026-05-10")
                .when().get("/reservations/theme/1/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].startAt", is("11:00:00"));
    }

    @Test
    void 인기_테마_조회() {
        LocalDate today = LocalDate.now();

        createTheme("미술관의 밤");
        createTheme("심해 연구소");
        createTheme("폐병원 탈출");

        createReservationTime("10:00", 1L);
        createReservationTime("11:00", 2L);
        createReservationTime("12:00", 3L);

        createReservation("쿠다", today.minusDays(1).toString(), 1L);
        createReservation("아루", today.minusDays(2).toString(), 1L);
        createReservation("도기", today.minusDays(3).toString(), 1L);

        createReservation("포비", today.minusDays(1).toString(), 2L);
        createReservation("솔라", today.minusDays(2).toString(), 2L);

        createReservation("레오", today.minusDays(1).toString(), 3L);
        createReservation("오래된예약", today.minusDays(10).toString(), 3L);

        RestAssured.given().log().all()
                .queryParam("period", 7)
                .queryParam("limit", 2)
                .when().get("/reservations/theme/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].name", is("미술관의 밤"))
                .body("[1].name", is("심해 연구소"));
    }

    private void createTheme() {
        createTheme("미술관의 밤");
    }

    private void createTheme(final String name) {
        Map<String, String> theme = new HashMap<>();
        theme.put("name", name);
        theme.put("description", "추리 테마");
        theme.put("thumbnailUrl", "https://example.com/theme.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);
    }

    private void createReservationTime(final String startAt, final Long themeId) {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", startAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/themes/" + themeId + "/times")
                .then().log().all()
                .statusCode(201);
    }

    private void createReservation(final String name, final String date, final Long timeId) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("date", date);
        reservation.put("timeId", timeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    private void clearTables() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }
}
