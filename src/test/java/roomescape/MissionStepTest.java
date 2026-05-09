package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.ReservationController;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.test.util.TestDatabaseUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    private static final int INITIALIZED_RESERVATION_COUNT = 3;
    private static final int INITIALIZED_TIME_COUNT = 2;

    private static final UUID DATA_TIME_MORNING = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01");
    private static final UUID DATA_TIME_AFTERNOON = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa02");

    private static final UUID DATA_THEME_WESTERN = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb01");

    private static final UUID TEST_TIME_ID = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddd01");
    private static final UUID TEST_THEME_ID = UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01");

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationController reservationController;

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(INITIALIZED_RESERVATION_COUNT));
    }

    @Test
    void 예약_추가_및_삭제() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
        insertTestTimeAndTheme();

        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", TEST_TIME_ID.toString());
        params.put("themeId", TEST_THEME_ID.toString());

        String createdReservationId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/" + createdReservationId)
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 데이터베이스_연동() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void DB_조회_API_전환() {
        TestDatabaseUtils.clearTables(jdbcTemplate);

        UUID reservationId = UUID.fromString("99999999-9999-9999-9999-999999990001");
        String name = "브라운";
        String date = "2023-08-05";

        jdbcTemplate.update(
                "INSERT INTO reservation_time (id, start_at) VALUES (?, ?)",
                TEST_TIME_ID.toString(),
                LocalTime.of(10, 0)
        );
        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, image_url) VALUES (?, ?, ?, ?)",
                TEST_THEME_ID.toString(),
                "themeName",
                "themeDescription",
                "themeUrl"
        );
        jdbcTemplate.update(
                "INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (?, ?, ?, ?, ?)",
                reservationId.toString(),
                name,
                date,
                TEST_TIME_ID.toString(),
                TEST_THEME_ID.toString()
        );

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", equalTo(reservationId.toString()))
                .body("[0].name", is(name))
                .body("[0].date", is(date))
                .body("[0].time.id", equalTo(TEST_TIME_ID.toString()));

        Integer actualCount = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(actualCount).isEqualTo(1);
    }

    @Test
    void DB_추가_삭제_API_전환() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
        insertTestTimeAndTheme();

        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", TEST_TIME_ID.toString());
        params.put("themeId", TEST_THEME_ID.toString());

        String createdReservationId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/reservations/" + createdReservationId)
                .then().log().all()
                .statusCode(200);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    void 시간_관리_API() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        String createdTimeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/times/" + createdTimeId)
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 예약과_시간_연결() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
        insertTestTimeAndTheme();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", TEST_TIME_ID.toString());
        reservation.put("themeId", TEST_THEME_ID.toString());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 계층화_리팩터링() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    @Test
    void 예약_가능한_시간_조회() {
        Map<String, String> availableTimesParams = Map.of(
                "themeId", DATA_THEME_WESTERN.toString(),
                "date", "2026-05-05"
        );
        List<ReservationTimeResponse> expectedAvailableTimes = List.of(
                new ReservationTimeResponse(DATA_TIME_MORNING, LocalTime.parse("10:00:00")),
                new ReservationTimeResponse(DATA_TIME_AFTERNOON, LocalTime.parse("14:00:00"))
        );

        List<ReservationTimeResponse> actualAvailableTimes = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .params(availableTimesParams)
                .when().get("/times/available-times")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ReservationTimeResponse.class);

        assertThat(actualAvailableTimes).containsExactlyInAnyOrderElementsOf(expectedAvailableTimes);
    }

    @Test
    void 이미_예약된_시간은_조회에서_제외() {
        UUID reservedTimeId = DATA_TIME_MORNING;

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "브라운",
                        "date", "2026-05-05",
                        "timeId", reservedTimeId.toString(),
                        "themeId", DATA_THEME_WESTERN.toString()
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        List<ReservationTimeResponse> availableTimes = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .params(Map.of(
                        "themeId", DATA_THEME_WESTERN.toString(),
                        "date", "2026-05-05"
                ))
                .when().get("/times/available-times")
                .then().log().all()
                .extract().jsonPath().getList(".", ReservationTimeResponse.class);

        boolean reservedTimeNotExist = availableTimes.stream()
                .noneMatch(availableTime -> reservedTimeId.equals(availableTime.id()));
        assertThat(reservedTimeNotExist).isTrue();
        assertThat(availableTimes).hasSize(INITIALIZED_TIME_COUNT - 1);
    }

    private void insertTestTimeAndTheme() {
        jdbcTemplate.update(
                "INSERT INTO reservation_time (id, start_at) VALUES (?, ?)",
                TEST_TIME_ID.toString(),
                LocalTime.of(10, 0)
        );
        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, image_url) VALUES (?, ?, ?, ?)",
                TEST_THEME_ID.toString(),
                "themeName",
                "themeDescription",
                "themeUrl"
        );
    }
}
