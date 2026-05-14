package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationE2ETest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "공포", "무서운 테마", "thumb.png");
    }

    @Test
    @DisplayName("GET /admin/reservations - 예약 목록을 조회한다")
    void getReservations() {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "브라운", "2025-12-25", 1L, 1L);

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("브라운"))
                .body("[0].date", is("2025-12-25"))
                .body("[0].time.id", is(1))
                .body("[0].theme.id", is(1))
                .body("[0].theme.name", is("공포"));
    }

    @Test
    @DisplayName("POST /reservations - 예약을 생성하면 201과 ResponseReservation을 반환한다")
    void createReservation() {
        Map<String, Object> body = new HashMap<>();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        body.put("name", "브라운");
        body.put("date", tomorrow.toString());
        body.put("timeId", 1);
        body.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is("브라운"))
                .body("date", is(tomorrow.toString()))
                .body("time.id", is(1))
                .body("theme.id", is(1));
    }

    @Test
    @DisplayName("POST /reservations - 존재하지 않는 시간 ID로 예약을 생성하면 404를 반환한다")
    void createReservation_invalidTimeId() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "브라운");
        body.put("date", "2025-12-25");
        body.put("timeId", 999);
        body.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("GET /reservation?name=어셔 - 본인 예약 목록을 조회한다")
    void getUserReservations() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate dayAfter = LocalDate.now().plusDays(2);

        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "14:00");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 2L, 1L);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔2", dayAfter.toString(), 1L, 1L);

        RestAssured.given().log().all()
                .queryParam("name", "어셔")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("name", everyItem(is("어셔")));
    }

    @Test
    @DisplayName("GET /reservations - name 파리미터가 없으면 400 반환한다")
    void getUserReservations_noNameQueryParameter() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("MISSING_PARAMETER"));
    }

    @Test
    @DisplayName("GET /reservations?name=테스트 - 사용자 예약이 없으면 빈 리스트를 반환한다")
    void getUserReservation_noMatchReservation() {
        RestAssured.given().log().all()
                .queryParam("name", "테스트")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("DELETE /reservations/{id}?name=어셔 - 본인의 미래 예약을 취소한다")
    void cancelUserReservation() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);

        RestAssured.given().log().all()
                .queryParam("name", "어셔")
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservation WHERE id = 1", Integer.class);
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("DELETE /reservations/{id} - 존재하지 않는 예약 취소 시 404을 반환한다")
    void cancelUserReservation_notFound() {
        RestAssured.given().log().all()
                .queryParam("name", "어셔")
                .when().delete("/reservations/999")
                .then().log().all()
                .statusCode(404)
                .body("errorCode", is("RESERVATION_NOT_FOUND"));
    }

    @Test
    @DisplayName("DELETE /reservations/{id} - 본인이 아닌 예약 취소 시 403을 반환한다")
    void cancelUserReservation_forbidden() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);

        RestAssured.given().log().all()
                .queryParam("name", "레서")
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(403)
                .body("errorCode", is("RESERVATION_FORBIDDEN"));
    }

    @Test
    @DisplayName("DELETE /reservations/{id} - 이미 지난 예약 취소 시 400 EXPIRED를 반환한다")
    void cancelUserReservation_expired() {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", LocalDate.now().minusDays(1).toString(), 1L, 1L);

        RestAssured.given().log().all()
                .queryParam("name", "어셔")
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("RESERVATION_EXPIRED"));
    }

    @Test
    @DisplayName("PATCH /reservations/{id}?name=어셔 - 본인 예약 날짜를 변경한다")
    void updateUserReservation() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate newDate = LocalDate.now().plusDays(3);
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "14:00");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);

        Map<String, Object> body = new HashMap<>();
        body.put("date", newDate.toString());
        body.put("timeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "어셔")
                .body(body)
                .when().patch("/reservations/1")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("date", is(newDate.toString()))
                .body("time.id", is(2));
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 존재하지 않으면 404를 반환한다")
    void updateUserReservation_notFound() {
        Map<String, Object> body = new HashMap<>();
        body.put("date", LocalDate.now().plusDays(3).toString());
        body.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "어셔")
                .body(body)
                .when().patch("/reservations/999")
                .then().log().all()
                .statusCode(404)
                .body("errorCode", is("RESERVATION_NOT_FOUND"));
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 본인의 예약이 아니면 403을 반환한다")
    void updateUserReservation_forbidden() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);

        Map<String, Object> body = new HashMap<>();
        body.put("date", LocalDate.now().plusDays(3).toString());
        body.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "어셔2")
                .body(body)
                .when().patch("/reservations/1")
                .then().log().all()
                .statusCode(403)
                .body("errorCode", is("RESERVATION_FORBIDDEN"));
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 만료된 예약은 변경 불가 400를 반환한다")
    void updateUserReservation_expired() {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", LocalDate.of(2020, 1, 1).toString(), 1L, 1L);

        Map<String, Object> body = new HashMap<>();
        body.put("date", LocalDate.now().plusDays(3).toString());
        body.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "어셔")
                .body(body)
                .when().patch("/reservations/1")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("RESERVATION_EXPIRED"));
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 시간 데이터가 없다면 404를 반환한다")
    void updateUserReservation_timeNotFound() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);

        Map<String, Object> body = new HashMap<>();
        body.put("date", LocalDate.now().plusDays(3).toString());
        body.put("timeId", 999);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "어셔")
                .body(body)
                .when().patch("/reservations/1")
                .then().log().all()
                .statusCode(404)
                .body("errorCode", is("RESERVATION_TIME_NOT_FOUND"));
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 과거 일시로 변경 시 400 PAST_DATETIME을 반환한다")
    void updateUserReservation_pastDateTime() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);

        Map<String, Object> body = new HashMap<>();
        body.put("date", LocalDate.of(2020, 1, 1).toString());
        body.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "어셔")
                .body(body)
                .when().patch("/reservations/1")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("RESERVATION_PAST_DATETIME"));
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 동일 일시로 변경 시도 시 409")
    void updateUserReservation_sameDateTime() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);

        Map<String, Object> body = new HashMap<>();
        body.put("date", tomorrow.toString());
        body.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "어셔")
                .body(body)
                .when().patch("/reservations/1")
                .then().log().all()
                .statusCode(409)
                .body("errorCode", is("RESERVATION_DUPLICATE"));
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 다른 예약과 중복되면 409")
    void updateUserReservation_conflictsWithOther() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate dayAfter = LocalDate.now().plusDays(2);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔2", dayAfter.toString(), 1L, 1L);

        Map<String, Object> body = new HashMap<>();
        body.put("date", dayAfter.toString());
        body.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "어셔")
                .body(body)
                .when().patch("/reservations/1")
                .then().log().all()
                .statusCode(409)
                .body("errorCode", is("RESERVATION_DUPLICATE"));
    }

    @Test
    @DisplayName("POST /reservations - 빈 이름으로 400 요청하면 INVALID_INPUT과 fieldErrors를 반환한다")
    void createReservation_blankName() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "");
        body.put("date", LocalDate.now().plusDays(1).toString());
        body.put("timeId", 1);
        body.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("INVALID_INPUT"))
                .body("fieldErrors.field", hasItem("name"));
    }

    @Test
    @DisplayName("POST /reservations - 과거 일시로 예약 시 400 PAST_DATETIME을 반환한다")
    void createReservation_pastDateTime() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "어셔");
        body.put("date", LocalDate.of(2020, 1, 1).toString());
        body.put("timeId", 1);
        body.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("RESERVATION_PAST_DATETIME"));
    }

    @Test
    @DisplayName("POST /reservations - 같은 날짜+시간+테마 중복은 409 DUPLICATE를 반환한다")
    void createReservation_duplicate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);

        Map<String, Object> body = new HashMap<>();
        body.put("name", "어셔2");
        body.put("date", tomorrow.toString());
        body.put("timeId", 1);
        body.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("errorCode", is("RESERVATION_DUPLICATE"));
    }
}
