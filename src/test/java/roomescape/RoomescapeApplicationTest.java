package roomescape;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomescapeApplicationTest {

        private static final String FUTURE_DATE_TEXT = LocalDate.now().plusDays(1).toString();
        private static final String PAST_DATE_TEXT = LocalDate.now().minusDays(1).toString();

        @Autowired
        JdbcTemplate jdbcTemplate;

        @Test
        void contextLoads() {

        }

        @DisplayName("저장된 모든 예약을 응답한다.")
        @Test
        void canResponseAllReservations() {
                jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1, LocalTime.of(10,0));
                jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 2, LocalTime.of(11,0));

                jdbcTemplate.update("insert into reservation (id, name, date, time_id) values (?, ?, ?, ?)", 1, "랜디", "2025-5-5", "1");
                jdbcTemplate.update("insert into reservation (id, name, date, time_id) values (?, ?, ?, ?)", 2, "아마", "2025-5-5", "2");

                RestAssured.given().log().all()
                        .when().get("/reservations")
                        .then().log().all()
                        .statusCode(200)
                        .body("size()", is(2));
        }

        @DisplayName("예약을 추가할 수 있다.")
        @Test
        void canCreateReservation() {
                jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

                Map<String, Object> params = new HashMap<>();
                params.put("name", "브라운");
                params.put("date", FUTURE_DATE_TEXT);
                params.put("timeId", 1);

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/reservations")
                        .then().log().all()
                        .statusCode(201)
                        .header("location", "reservations/1");
        }

        @DisplayName("이미 예약된 날짜와 시간으로는 예약이 불가능하다.")
        @Test
        void cannotCreateReservationsWhenDuplicatedTime() {
                jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1, LocalTime.of(10,0));
                jdbcTemplate.update("insert into reservation (id, name, date, time_id) values (?, ?, ?, ?)", 1, "랜디", FUTURE_DATE_TEXT, "1");

                Map<String, Object> params = new HashMap<>();
                params.put("name", "브라운");
                params.put("date", FUTURE_DATE_TEXT);
                params.put("timeId", 1);

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/reservations")
                        .then().log().all()
                        .statusCode(400);
        }

        @DisplayName("유효하지 않은 입력값으로 예약 추가가 불가능하다.")
        @Test
        void cannotCreateReservationsWhenInvalidRequest() {
                jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1, LocalTime.of(10,0));

                Map<String, Object> params = new HashMap<>();
                params.put("name", "브라운");
                params.put("date", "");
                params.put("timeId", 1);

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/reservations")
                        .then().log().all()
                        .statusCode(400);
        }

        @DisplayName("유효하지 않은 입력값으로 예약 추가가 불가능하다.")
        @Test
        void cannotCreateReservationsWhenPastRequest() {
                jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1, LocalTime.of(10,0));

                Map<String, Object> params = new HashMap<>();
                params.put("name", "브라운");
                params.put("date", PAST_DATE_TEXT);
                params.put("timeId", 1);

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/reservations")
                        .then().log().all()
                        .statusCode(400);
        }

        @DisplayName("Id를 통해 예약을 삭제할 수 있다.")
        @Test
        void canDeleteReservationById() {
                jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1, LocalTime.of(10,0));
                jdbcTemplate.update("insert into reservation (id, name, date, time_id) values (?, ?, ?, ?)", 1, "랜디", "2025-5-5", "1");

                RestAssured.given().log().all()
                        .when().delete("/reservations/1")
                        .then().log().all()
                        .statusCode(204);
        }


}
