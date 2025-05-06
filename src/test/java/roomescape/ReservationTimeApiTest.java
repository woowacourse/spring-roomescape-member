package roomescape;

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
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeApiTest {

    private static final String FUTURE_DATE_TEXT = LocalDate.now().plusDays(1).toString();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");

        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("예약 가능한 시간을 추가할 수 있다.")
    @Test
    void canCreateReservationTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("location", "times/1");
    }

    @DisplayName("이미 존재하는 예약 가능 시간은 추가할 수 없다.")
    @Test
    void cannotCreateReservationTimeWhenExist() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("유효하지 않은 요청을 예약 가능 시간을 추가할 수 없다")
    @Test
    void cannotCreateReservationTimeWhenInvalidRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 가능 시간을 조회할 수 있다")
    @Test
    void canResponseAllReservationTimes() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00");

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("예약 여부와 함께 예약 가능 시간을 조회할 수 있다")
    @Test
    void canResponseAvaliableReservationTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)", "이름1", "설명1",
                "썸네일1");
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                FUTURE_DATE_TEXT, "1", "1");

        RestAssured.given().log().all()
                .when().get("/" + FUTURE_DATE_TEXT + "/1" + "/times")
                .then().log().all()
                .statusCode(200)
                .body("get(0).bookState", is(true));

        jdbcTemplate.update("delete from reservation");

        RestAssured.given().log().all()
                .when().get("/" + FUTURE_DATE_TEXT + "/1" + "/times")
                .then().log().all()
                .statusCode(200)
                .body("get(0).bookState", is(false));
    }

    @DisplayName("예약 가능한 시간을 삭제할 수 있다")
    @Test
    void canDeleteReservationTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 시간을 삭제할 수 없다.")
    @Test
    void cannotDeleteReservationTimeWhenNotExist() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        RestAssured.given().log().all()
                .when().delete("/times/2")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("이미 해당 시간에 대해 예약 데이터가 존재한다면 삭제가 불가능하다")
    @Test
    void cannotDeleteReservationTimeWhenExistReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)", "이름1", "설명1",
                "썸네일1");
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                FUTURE_DATE_TEXT, "1", "1");

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }
}
