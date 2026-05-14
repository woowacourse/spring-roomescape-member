package roomescape.admin;

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

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminE2ETest {

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
    @DisplayName("DELETE /admin/times/{id} - 예약이 있는 시간은 삭제 불가 409 반환한다")
    void removeTime_inUse() {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", LocalDate.now().plusDays(1).toString(), 1L, 1L);

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(409)
                .body("errorCode", is("RESERVATION_TIME_IN_USE"));
    }

    @Test
    @DisplayName("DELETE /admin/themes/{id} - 예약이 있는 시간은 삭제 불가 409 반환한다")
    void removeTheme_inUse() {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", LocalDate.now().plusDays(1).toString(), 1L, 1L);

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(409)
                .body("errorCode", is("THEME_IN_USE"));
    }

    @Test
    @DisplayName("POST /admin/themes - 이름이 빈 문자열이면 400 INVALID_INPUT와 fieldErrors를 반환한다")
    void createTheme_blankName() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "");
        body.put("description", "무서운 테마");
        body.put("thumbnail", "thumb.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("INVALID_INPUT"))
                .body("fieldErrors.field", hasItem("name"));
    }

    @Test
    @DisplayName("POST /admin/themes - 모든 필드가 null이면 fieldErrors에 3개 모두 담긴다")
    void createTheme_allFieldsNull() {
        Map<String, String> body = new HashMap<>();
        body.put("name", null);
        body.put("description", null);
        body.put("thumbnail", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("INVALID_INPUT"))
                .body("fieldErrors.size()", is(3));
    }

    @Test
    @DisplayName("POST /admin/times - 시작 시간이 null이면 400 INVALID_INPUT을 반환한다")
    void createTime_nullStartAt() {
        Map<String, String> body = new HashMap<>();
        body.put("startAt", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("INVALID_INPUT"))
                .body("fieldErrors.field", hasItem("startAt"));
    }

    @Test
    @DisplayName("POST /admin/times - 잘못된 시간 형식이면 400 INVALID_REQUEST_FORMAT을 반환한다")
    void createTime_invalidTimeFormat() {
        Map<String, String> body = new HashMap<>();
        body.put("startAt", "10시");   // HH:mm 형식 아님

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("INVALID_REQUEST_FORMAT"));
    }

    @Test
    @DisplayName("GET /admin/reservations - 예약이 없으면 빈 배열을 반환한다")
    void getReservations_empty() {
        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("GET /admin/reservations - 사용자와 무관하게 모든 예약을 조회한다")
    void getReservations_returnsAllUsers() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate dayAfter = LocalDate.now().plusDays(2);

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "14:00");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "어셔", tomorrow.toString(), 1L, 1L);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "레서", dayAfter.toString(), 2L, 1L);

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("name", hasItems("어셔", "레서"));
    }
}
