package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 예약_추가_및_삭제() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00");

        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1);
        params.put("date", "2023-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations/my?userId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1?userId=1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations/my?userId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void DB_추가_삭제_API_전환() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00");

        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1);
        params.put("date", "2023-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        assertThat(jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class))
                .isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/reservations/1?userId=1")
                .then().log().all()
                .statusCode(200);

        assertThat(jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class))
                .isEqualTo(0);
    }

    @Test
    void 예약과_시간_연결() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00");

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("userId", 1);
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations/my?userId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 이미_예약된_시간_예약_불가() {
        insertUser(1L, "홍길동", "hong@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");
        insertReservation(1L, 1L, "2026-05-06", 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1);
        params.put("date", "2026-05-06");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("해당 날짜·시간·테마에 이미 예약이 존재합니다."));
    }

    @Test
    void 타인의_예약_삭제_시_403_반환() {
        insertUser(1L, "홍길동", "hong@test.com");
        insertUser(2L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");
        insertReservation(1L, 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .when().delete("/reservations/1?userId=2")
                .then().log().all()
                .statusCode(403)
                .body(equalTo("본인의 예약만 삭제할 수 있습니다."));
    }

    private void insertUser(Long id, String name, String email) {
        jdbcTemplate.update("INSERT INTO users(id, name, email) VALUES (?, ?, ?)", id, name, email);
    }

    private void insertTheme(Long id, String name) {
        jdbcTemplate.update(
                "INSERT INTO theme(id, name, description, thumbnail_image_url) VALUES (?, ?, '설명', 'https://thumbnail.url')",
                id, name);
    }

    private void insertReservationTime(Long id, String startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time(id, start_at) VALUES (?, ?)", id, startAt);
    }

    private void insertReservation(Long userId, Long themeId, String date, Long timeId) {
        jdbcTemplate.update("INSERT INTO reservation(user_id, theme_id, date, time_id) VALUES (?, ?, ?, ?)",
                userId, themeId, date, timeId);
    }
}
