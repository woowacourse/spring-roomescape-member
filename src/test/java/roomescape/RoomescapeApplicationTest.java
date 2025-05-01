package roomescape;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import java.time.LocalDate;
import java.time.LocalTime;
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
class RoomescapeApplicationTest {

        private static final String FUTURE_DATE_TEXT = LocalDate.now().plusDays(1).toString();
        private static final String PAST_DATE_TEXT = LocalDate.now().minusDays(1).toString();

        @Autowired
        JdbcTemplate jdbcTemplate;

        @Test
        void contextLoads() {

        }

        @BeforeEach
        void setUp() {
                jdbcTemplate.update("DELETE FROM reservation");
                jdbcTemplate.update("DELETE FROM reservation_time");
                jdbcTemplate.update("DELETE FROM theme");

                jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
                jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
                jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        }

        @DisplayName("저장된 모든 예약을 응답한다.")
        @Test
        void canResponseAllReservations() {
                jdbcTemplate.update("insert into reservation_time (start_at) values (?)", LocalTime.of(10, 0));
                jdbcTemplate.update("insert into reservation_time (start_at) values (?)", LocalTime.of(11, 0));

                jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)", "이름1", "설명1",
                        "썸네일1");
                jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)", "이름2", "설명2",
                        "썸네일2");

                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                        "2025-5-5", 1, 1);
                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "아마",
                        "2025-5-5", 2, 2);

                RestAssured.given().log().all()
                        .when().get("/reservations")
                        .then().log().all()
                        .statusCode(200)
                        .body("size()", is(2));
        }

        @DisplayName("예약을 추가할 수 있다.")
        @Test
        void canCreateReservation() {
                jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)", "이름1", "설명1",
                        "썸네일1");
                jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

                Map<String, Object> params = new HashMap<>();
                params.put("name", "브라운");
                params.put("date", FUTURE_DATE_TEXT);
                params.put("timeId", 1);
                params.put("themeId", 1);

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/reservations")
                        .then().log().all()
                        .statusCode(201)
                        .header("location", "reservations/1");
        }

        @DisplayName("유효하지 않은 입력값으로 예약 추가가 불가능하다.")
        @Test
        void cannotCreateReservationsWhenInvalidRequest() {
                jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1,
                        LocalTime.of(10, 0));

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

        @DisplayName("과거의 날짜와 시간으로는 예약이 불가능하다")
        @Test
        void cannotCreateReservationsWhenPastRequest() {
                jdbcTemplate.update("insert into reservation_time (start_at) values (?)", LocalTime.of(10, 0));

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

        @DisplayName("중복 예약을 추가할 수 없다.")
        @Test
        void cannotCreateReservationsWhenDuplicatedTime() {
                jdbcTemplate.update("insert into reservation_time (start_at) values (?)", LocalTime.of(10, 0));
                jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)", "이름", "설명",
                        "썸네일");
                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                        FUTURE_DATE_TEXT, "1", "1");

                Map<String, Object> params = new HashMap<>();
                params.put("name", "브라운");
                params.put("date", FUTURE_DATE_TEXT);
                params.put("timeId", 1);
                params.put("themeId", 1);

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
                jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)", "이름1", "설명1",
                        "썸네일1");
                jdbcTemplate.update("insert into reservation_time (start_at) values (?)", LocalTime.of(10, 0));
                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                        "2025-5-5", "1", "1");

                RestAssured.given().log().all()
                        .when().delete("/reservations/1")
                        .then().log().all()
                        .statusCode(204);
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

        @DisplayName("유요하지 않은 요청을 예약 가능 시간을 추가할 수 없다")
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

        @DisplayName("테마를 추가할 수 있다.")
        @Test
        void canCreateTheme() {
                Map<String, String> params = new HashMap<>();
                params.put("name", "이름");
                params.put("description", "설명");
                params.put("thumbnail", "썸네일");

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/themes")
                        .then().log().all()
                        .statusCode(201)
                        .header("location", "theme/1");
        }

        @DisplayName("유효하지 않은 요청으로는 테마를 추가할 수 없다.")
        @Test
        void cannotCreateThemeWhenInvalidRequest() {
                Map<String, String> params = new HashMap<>();
                params.put("name", "이름");
                params.put("description", "");
                params.put("thumbnail", "썸네일");

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/themes")
                        .then().log().all()
                        .statusCode(400);
        }

        @DisplayName("모든 테마를 조회할 수 있다")
        @Test
        void canResponseAllTheme() {
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명",
                        "썸네일");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명",
                        "썸네일");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명",
                        "썸네일");

                RestAssured.given().log().all()
                        .when().get("/themes")
                        .then().log().all()
                        .statusCode(200)
                        .body("size()", is(3));
        }

        @DisplayName("모든 테마를 조회할 수 있다")
        @Test
        void canResponseTopThemes() {
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "인기테마", "설명1",
                        "썸네일1");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "평범테마", "설명2",
                        "썸네일2");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "인기없는테마", "설명3",
                        "썸네일3");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마1", "설명1",
                        "썸네일1");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마2", "설명2",
                        "썸네일2");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마3", "설명3",
                        "썸네일3");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마4", "설명1",
                        "썸네일1");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마5", "설명2",
                        "썸네일2");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마6", "설명3",
                        "썸네일3");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마7", "설명1",
                        "썸네일1");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마8", "설명2",
                        "썸네일2");
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마9", "설명3",
                        "썸네일3");

                jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
                jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00");
                jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "12:00");

                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                        PAST_DATE_TEXT, 1, 1);
                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                        PAST_DATE_TEXT, 2, 1);
                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                        PAST_DATE_TEXT, 3, 1);
                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                        PAST_DATE_TEXT, 1, 2);
                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                        PAST_DATE_TEXT, 2, 2);
                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                        PAST_DATE_TEXT, 1, 3);

                RestAssured.given().log().all()
                        .when().get("/themes/top")
                        .then().log().all()
                        .statusCode(200)
                        .body("get(0).name", is("인기테마"))
                        .body("get(1).name", is("평범테마"))
                        .body("get(2).name", is("인기없는테마"))
                        .body("size()", is(10));
        }

        @DisplayName("ID를 통해 테마를 삭제할 수 있다")
        @Test
        void canDeleteThemeById() {
                jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명",
                        "썸네일");

                RestAssured.given().log().all()
                        .when().delete("/themes/1")
                        .then().log().all()
                        .statusCode(204);
        }

        @DisplayName("이미 테마에 대한 예약이 존재한다면 해당 테마의 삭제가 불가능하다.")
        @Test
        void cannotDeleteThemeByIdWhenReservationExist() {
                jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
                jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)", "이름1", "설명1",
                        "썸네일1");
                jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)", "랜디",
                        FUTURE_DATE_TEXT, "1", "1");

                RestAssured.given().log().all()
                        .when().delete("/themes/1")
                        .then().log().all()
                        .statusCode(400);
        }
}
